package org.example.myspringsecurity.core.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.myspringsecurity.core.security.v1.SecurityConfigRegistry;
import org.example.myspringsecurity.core.security.v1.SecurityRule;
import org.example.myspringsecurity.model.entity.Permissons;
import org.example.myspringsecurity.model.entity.Role;
import org.example.myspringsecurity.model.entity.User;
import org.example.myspringsecurity.securityLib.core.Authentication;
import org.example.myspringsecurity.securityLib.core.GrantedAuthority;
import org.example.myspringsecurity.securityLib.core.context.SecurityContextHolder;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetails;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class AuthorizationFilter implements Filter {
    private final SecurityConfigRegistry securityConfigRegistry;

    public AuthorizationFilter(SecurityConfigRegistry securityConfigRegistry) {
        this.securityConfigRegistry = securityConfigRegistry;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        Optional<SecurityRule> ruleOptional = securityConfigRegistry.getRuleForPath(path);

        try {
            if (ruleOptional.isPresent()) {
                SecurityRule rule = ruleOptional.get();

                if (rule.isPermitAll()) {
                    System.err.println("Authentication is permitted");
                    chain.doFilter(request, response);
                    return;
                }

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (rule.requiresAuthentication()) {
                    if (authentication == null || !authentication.isAuthenticated()) {
                        System.err.println("Access Denied: " + path + " - Requires authentication."+authentication.isAuthenticated());
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
                        return;
                    }
                }

                if (authentication == null || !hasRequiredRole(authentication, rule.getRequiredRole())) {
                    System.err.println("Access Denied: " + path + " - Insufficient permissions. Requires role: " +
                            rule.getRequiredRole() + " role is::" + authentication.getAuthorities());
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient permissions");
                    return;
                }
                chain.doFilter(request, response);
            } else {
                System.err.println("Access Denied: " + path + " - No security rule defined.");
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            }
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    private boolean hasRequiredRole(Authentication authentication, String requiredRole) {
        if (authentication == null || requiredRole == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(requiredRole));
    }

}