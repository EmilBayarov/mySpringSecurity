package org.example.myspringsecurity.core.config;

import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.securityLib.authentication.auth.AuthenticationManager;

import org.example.myspringsecurity.securityLib.authentication.auth.AuthenticationProvider;
import org.example.myspringsecurity.securityLib.authentication.auth.DaoAuthenticationProvider;
import org.example.myspringsecurity.securityLib.authentication.auth.ProviderManager;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;
import org.example.myspringsecurity.securityLib.crypto.password.BCryptPasswordEncoder;
import org.example.myspringsecurity.securityLib.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}
