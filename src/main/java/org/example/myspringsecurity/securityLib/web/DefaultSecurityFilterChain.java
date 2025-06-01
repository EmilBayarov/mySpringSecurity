package org.example.myspringsecurity.securityLib.web;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.example.myspringsecurity.securityLib.web.matcher.RequestMatcher;

import java.util.List;

public class DefaultSecurityFilterChain implements SecurityFilterChain {
    private final RequestMatcher requestMatcher;
    private final List<Filter> filters;

    public DefaultSecurityFilterChain(RequestMatcher requestMatcher, List<Filter> filters) {
        this.requestMatcher = requestMatcher;
        this.filters = filters;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return this.requestMatcher.matches(request);
    }

    @Override
    public List<Filter> getFilters() {
        return this.filters;
    }

    public RequestMatcher getRequestMatcher() {
        return this.requestMatcher;
    }
}
