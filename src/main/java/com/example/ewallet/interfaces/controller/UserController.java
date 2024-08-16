package com.example.ewallet.interfaces.controller;

import com.example.ewallet.application.dto.UserDto;
import com.example.ewallet.application.dto.LoginDto;
import com.example.ewallet.application.dto.UpdatePasswordDto;
import com.example.ewallet.application.service.kafka.KafkaProducerService;
import com.example.ewallet.application.service.UserService;
import com.example.ewallet.domains.user.model.user.User;
import com.example.ewallet.infrastructure.configuration.annonation.LoginRequired;
//import com.example.ewallet.infrastructure.configuration.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/register")
    @LoginRequired
    public ResponseEntity<User> register(@RequestBody UserDto userDto) {
        User user = userService.register(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getPhoneNumber()
        );
        // 发送 kafka 消息
        kafkaProducerService.sendMessage("User registered: " + user.getUsername());
        return ResponseEntity.ok(user);
    }

    /**
     *  @Data
     * public class LoginDto {
     *     private String email;
     *     private String password;
     * }
     */
    @PostMapping("/login")
    @LoginRequired
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            Optional<User> userAccountOpt = userService.login(loginDto.getEmail(), loginDto.getPassword());
            if(!userAccountOpt.isPresent()) {
                return ResponseEntity.status(401).body("Login - Invalid email or password");
            }
            // 发送 kafka 消息
            kafkaProducerService.sendMessage("User logged in: " + loginDto.getEmail());
            User user = userAccountOpt.get();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Login Exception - Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LoginDto loginDto) {
//        String email = jwtUtil.getClaimsFromToken(token.substring(7)).getSubject();
        Long userId = userService.getUserIdByEmail(loginDto.getEmail());
        userService.logout(userId);
        return ResponseEntity.ok("Logged out");
    }

    @PutMapping("/{username}/username")
    public ResponseEntity<User> updateUsernameByUsername(@PathVariable String username, @RequestBody UpdatePasswordDto updatePasswordDto) {
        Optional<User> userAccountOpt = userService.updatePasswordByUsername(username, updatePasswordDto.getNewPassword());
        return userAccountOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
//        Optional<User> userAccountOpt = userService.getUserInfo(userId);
//        return userAccountOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
//        Long userId = userService.getUserIdByEmail(email);
        Optional<User> userAccountOpt = userService.getUserInfo(userId);
        return userAccountOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}