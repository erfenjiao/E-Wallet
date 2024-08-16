package com.example.ewallet.infrastructure.configuration.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.ewallet.infrastructure.configuration.annonation.LoginRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOGGER.info("==>进入拦截器{}", handler);
        if (!(handler instanceof HandlerMethod)) {
            LOGGER.info("没有映射到方法，则无需检查直接通过");
            return true;
        }
        HandlerMethod heHandlerMethod = (HandlerMethod) handler;
        Method method = heHandlerMethod.getMethod();
        LOGGER.info("检查方法：{}", method.getName());

        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        LOGGER.info("=========>{}", loginRequired);
        LOGGER.info("===>检查是否需要登录，结果：{}", loginRequired != null ? "不需要登录" : "需要登录");

        if (loginRequired != null) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        LOGGER.info("=======>获取请求头：{}", authorizationHeader);
        LOGGER.info("=======>获取token：{}", token);

        if (token != null) {
            boolean flag = redisService.hasKey(token);
//            boolean flag = redisService.hasKey("token:5");
            LOGGER.info("=======>redis中token是否失效：{}", flag ? "未失效" : "失效" );
            if (flag) {
                return true;
            }
        }

        setHeader(request, response, "{\r\n" +
                "    \"code\": \"-1\",\r\n" +
                "    \"message\": \"请进行登录！\",\r\n" +
                "    \"data\": \"null\"\r\n" +
                "}");
        return false;
    }

    private void setHeader(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
