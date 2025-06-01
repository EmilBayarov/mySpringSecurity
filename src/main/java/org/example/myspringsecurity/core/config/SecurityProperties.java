package org.example.myspringsecurity.core.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.security")
public class SecurityProperties {
    private CookieSettings cookieSettings;
    @Data
    public static class CookieSettings {

        private String name = "refreshToken";
        private Boolean httpOnly = true;
        private Boolean secure = true;
        private Long maxAge = 2592000L; // 30 days
        private String sameSite = "Strict";
        private String path = "/api";
    }
}
