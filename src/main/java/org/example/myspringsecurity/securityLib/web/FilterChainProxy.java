package org.example.myspringsecurity.securityLib.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.example.myspringsecurity.securityLib.core.context.SecurityContextHolder;
import org.example.myspringsecurity.securityLib.core.context.SecurityContextHolderStrategy;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private static final String FILTER_APPLIED = FilterChainProxy.class.getName().concat(".APPLIED");
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private FilterChainDecorator filterChainDecorator = new VirtualFilterChainDecorator();
    private List<SecurityFilterChain> filterChains;
    public FilterChainProxy() {
    }

    public FilterChainProxy(SecurityFilterChain chain) {
        this(Arrays.asList(chain));
    }

    public FilterChainProxy(List<SecurityFilterChain> filterChains) {
        this.filterChains = filterChains;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        boolean clearContext = request.getAttribute(FILTER_APPLIED) == null;
        if (!clearContext) {
            doFilterInternal(request, response, filterChain);
            return;
        }
        try {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            doFilterInternal(request, response, filterChain);
        }
        catch (Exception ex) {
            Throwable[] causeChain;
        }
        finally {
            this.securityContextHolderStrategy.clearContext();
            request.removeAttribute(FILTER_APPLIED);
        }
    }

    private void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        List<Filter> filters = getFilters((HttpServletRequest) request);
        this.filterChainDecorator.decorate(chain).doFilter(request, response);
    }

    private List<Filter> getFilters(HttpServletRequest request) {
        for (SecurityFilterChain filterChain : this.filterChains) {
            if (filterChain.matches(request)) {
                return filterChain.getFilters();
            }
        }
        return null;
    }

    public interface FilterChainDecorator {
        default FilterChain decorate(FilterChain original) {
            return decorate(original, Collections.emptyList());
        }
        FilterChain decorate(FilterChain original, List<Filter> filters);
    }

    public static final class VirtualFilterChainDecorator implements FilterChainDecorator {

        @Override
        public FilterChain decorate(FilterChain original, List<Filter> filters) {
            return new VirtualFilterChain(original, filters);
        }

        @Override
        public FilterChain decorate(FilterChain original) {
            return original;
        }
    }

    public static final class VirtualFilterChain implements FilterChain {
        private final FilterChain originalChain;

        private final List<Filter> additionalFilters;

        public VirtualFilterChain(FilterChain originalChain, List<Filter> additionalFilters) {
            this.originalChain = originalChain;
            this.additionalFilters = additionalFilters;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
            this.originalChain.doFilter(request, response);
        }
    }
}
