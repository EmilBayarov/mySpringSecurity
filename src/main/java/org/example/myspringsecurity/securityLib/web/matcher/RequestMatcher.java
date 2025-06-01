package org.example.myspringsecurity.securityLib.web.matcher;

import jakarta.servlet.http.HttpServletRequest;

public interface RequestMatcher {
    boolean matches(HttpServletRequest request);
}
