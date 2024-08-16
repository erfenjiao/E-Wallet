//package com.example.ewallet.infrastructure.configuration.auth;
//
//import com.example.ewallet.domains.user.model.user.Email;
//import com.example.ewallet.domains.user.repository.UserRepository;
//import io.jsonwebtoken.Claims;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.Filter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//@Component
//public class JwtFilter implements Filter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        System.out.println("JwtFilter");
////        HttpServletRequest httpRequest = (HttpServletRequest) request;
////        String authorizationHeader = httpRequest.getHeader("Authorization");
////
////        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
////            String token = authorizationHeader.substring(7);
////            Claims claims = jwtUtil.getClaimsFromToken(token);
////
////            if (claims != null && !jwtUtil.isTokenExpired(token)) {
////                String email = claims.getSubject();
////                Email email1 = new Email(email);
////                request.setAttribute("user", userRepository.findByEmail(email1));
////            }
////        }
////        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {}
//
//    @Override
//    public void destroy() {}
//}
