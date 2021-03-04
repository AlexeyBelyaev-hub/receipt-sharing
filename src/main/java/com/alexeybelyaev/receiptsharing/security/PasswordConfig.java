package com.alexeybelyaev.receiptsharing.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Password encoder should be detached in separate file for deployment
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
     return new BCryptPasswordEncoder(10);
    }


}
