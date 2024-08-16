package com.example.ewallet.application.service;

import com.example.ewallet.domains.account.model.Account.Account;
import com.example.ewallet.domains.user.model.user.User;
import com.example.ewallet.domains.user.model.user.Password;
import com.example.ewallet.domains.user.model.user.Email;
import com.example.ewallet.domains.account.repository.AccountRepository;
import com.example.ewallet.domains.user.repository.UserRepository;
import com.example.ewallet.infrastructure.configuration.auth.JwtUtil;
import com.example.ewallet.infrastructure.configuration.auth.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;


    //private static final Logger logger = (Logger) LoggerFactory.getLogger(UserService.class);

    Logger logger = LoggerFactory.getLogger(UserService.class);
    public User register(String username, String plainPassword, String email, String phoneNumber) {
        Email emailAddress = new Email(email);
        Password password = new Password(plainPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(emailAddress);
        user.setPhoneNumber(phoneNumber);

        user = userRepository.save(user);

        //同时创建账户
        Account account = new Account(user.getUserId());
        System.out.println("create account for user: {}"+account);
        accountRepository.save(account);

        return user;
    }


//    public Optional<User> login(String email, String plainPassword) {
////        Optional<User> userAccountOpt = userRepository.findByUsername(username);
//        // 通过邮件登录
//        Email email1 = new Email(email);
//        Optional<User> userAccountOpt = userRepository.findByEmail(email1);
//        if (userAccountOpt.isPresent()) {
//            User user = userAccountOpt.get();
//            if (user.getPassword().matches(plainPassword)) {
//                return Optional.of(user);
//            }
//        }
//        return Optional.empty();
//    }

    /**
     *  return new ApiResponse<>(false, null, "密码不匹配"); // 密码不匹配
     *             }
     *         } else {
     *             logger.info("userOpt.isPresent() fail");
     *             return new ApiResponse<>(false, null, "用户不存在"); // 用户不存在
     *         }
     *     }
     * }
     * @param email
     * @param plainPassword
     * @return
     */
    public Optional<User> login(String email, String plainPassword) {
        Email email1 = new Email(email);
        Optional<User> userOpt = userRepository.findByEmail(email1);
        logger.info("login email: "+email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().matches(plainPassword)) {
                try {
                    String token = jwtUtil.generateToken(user.getUserId());

                    redisService.storeToken(token, token);
                    logger.info("redis's token: "+ redisService.getToken(user.getUserId()));
//                    redisService.getToken(user.getUserId());
                    //return user.getUserId().toString();
                    user.setToken(token);
                    return Optional.of(user);
                } catch (Exception e) {
                    // 根据实际需要记录错误日志或者进行其他错误处理
                    logger.error("Error generating or storing token for user: {}", user.getUserId(), e);
                    return Optional.empty(); // 或者抛出自定义异常，根据业务逻辑决定
                }
            } else {
                logger.error("password not match");
                return Optional.empty();
            }
        } else {
            logger.error("userOpt.isPresent() fail");
            return Optional.empty();
        }
    }

    public String logout(Long userId) {
        redisService.deleteToken(userId);
        return "logout success";
    }

    public Optional<User> updatePasswordByUsername(String username, String newPassword){
        return userRepository.findByUsername(username).map(user -> {
            user.setPassword(new Password(newPassword));
            return userRepository.save(user);
        });
    }

    public Optional<User> getUserInfo(Long userId) {
        return userRepository.findById(userId);
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(new Email(email)).get().getUserId();
    }

    //获取用户id
//    public Long getUserIdByUsername(String username){
//        return userRepository.findByUsername(username).get().getUserId();
//    }

}
