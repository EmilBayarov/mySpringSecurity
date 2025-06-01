package org.example.myspringsecurity.service;

import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.core.config.SecurityProperties;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {
    public static final String COOKIE_NAME = "refreshToken";
    private final SecurityProperties securityProperties;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        System.err.println(securityProperties.getCookieSettings());
        SecurityProperties.CookieSettings settings = securityProperties.getCookieSettings();
        return ResponseCookie.from(COOKIE_NAME, refreshToken)
                .httpOnly(settings.getHttpOnly())
                .secure(settings.getSecure())
                .path(settings.getPath())
                .maxAge(settings.getMaxAge())
                .sameSite(settings.getSameSite())
                .build();
    }

    public ResponseCookie createLogoutCookie() {
        SecurityProperties.CookieSettings settings = securityProperties.getCookieSettings();
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(settings.getHttpOnly())
                .secure(settings.getSecure())
                .path(settings.getPath())
                .maxAge(0)
                .sameSite(settings.getSameSite())
                .build();
    }
}
