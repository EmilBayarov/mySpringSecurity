package org.example.myspringsecurity.core.config;

import org.example.myspringsecurity.securityLib.crypto.password.BCryptPasswordEncoder;
import org.example.myspringsecurity.securityLib.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
