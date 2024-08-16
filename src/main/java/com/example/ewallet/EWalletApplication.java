package com.example.ewallet;

//import com.example.ewallet.infrastructure.configuration.auth.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class EWalletApplication {
    public static void main(String[] args) {
        SpringApplication.run(EWalletApplication.class, args);
    }

//    @Bean
//    public FilterRegistrationBean<JwtFilter> jwtFilter() {
//        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtFilter());
//        registrationBean.addUrlPatterns("/api/*"); // Apply filter to API endpoints
//        return registrationBean;
//    }
}
