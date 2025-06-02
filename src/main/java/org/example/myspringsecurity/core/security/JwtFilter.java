package org.example.myspringsecurity.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.model.entity.User;
import org.example.myspringsecurity.securityLib.authentication.UsernamePasswordAuthenticationToken;
import org.example.myspringsecurity.securityLib.core.context.SecurityContextHolder;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetails;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);
        if (userEmail!=null && SecurityContextHolder.getContext() != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.err.println("Context holdere set edildi:: "+SecurityContextHolder.getContext().getAuthentication().getName()+" role is "+authentication.getAuthorities());
            }
        }
        filterChain.doFilter(request, response);
    }
}
