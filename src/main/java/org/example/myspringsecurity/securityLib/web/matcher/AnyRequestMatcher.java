package org.example.myspringsecurity.securityLib.web.matcher;

import jakarta.servlet.http.HttpServletRequest;

public class AnyRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }
}
