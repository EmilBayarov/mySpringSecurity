package org.example.myspringsecurity.core.security;

import org.example.myspringsecurity.core.security.filter.AuthorizationFilter;
import org.example.myspringsecurity.core.security.filter.CorsFilter;
import org.example.myspringsecurity.core.security.v1.SecurityConfigRegistry;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityConfigRegistry securityConfigRegistry() {
        return new SecurityConfigRegistry();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthorizationFilter(securityConfigRegistry()));
        registration.addUrlPatterns("/*");
        registration.setOrder(4);
        return registration;
    }

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        return new JwtFilter(jwtService, userDetailsService);
    }

}
