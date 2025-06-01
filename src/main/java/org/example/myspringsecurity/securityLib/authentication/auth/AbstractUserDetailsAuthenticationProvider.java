package org.example.myspringsecurity.securityLib.authentication.auth;

import org.example.myspringsecurity.securityLib.authentication.AuthenticationException;
import org.example.myspringsecurity.securityLib.authentication.UsernamePasswordAuthenticationToken;
import org.example.myspringsecurity.securityLib.core.Authentication;
import org.example.myspringsecurity.securityLib.core.userDetails.NullUserCache;
import org.example.myspringsecurity.securityLib.core.userDetails.UserCache;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetails;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsChecker;

public abstract class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider {
    private UserCache userCache = new NullUserCache();
    private UserDetailsChecker preAuthenticationChecks  = new PreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new PostAuthenticationChecks();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = determineUsername(authentication);
        UserDetails user = this.userCache.getUserFromCache(username);
        if (user == null) {
            user = retrieveUser(username,(UsernamePasswordAuthenticationToken) authentication);
        }
        this.preAuthenticationChecks.check(user);
        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        this.postAuthenticationChecks.check(user);
        return createSuccessAuthentication(user, authentication, user);
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(
                principal, user.getPassword(), user.getAuthorities()
        );
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected abstract void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException;

    protected abstract UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException;

    private String determineUsername(Authentication authentication) {
        return (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getPrincipal().toString();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker userDetailsChecker) {
        this.preAuthenticationChecks = userDetailsChecker;
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return this.preAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker userDetailsChecker) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return this.postAuthenticationChecks;
    }

    private class PreAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails userDetails) {
            if (!userDetails.isAccountNonExpired()) {
                throw new RuntimeException("Account expired!");
            }
            if (!userDetails.isAccountNonLocked()) {
                throw new RuntimeException("Account is locked");
            }
            if (!userDetails.isEnabled()) {
                throw new RuntimeException("Account disabled");
            }
        }
    }

    private class PostAuthenticationChecks implements UserDetailsChecker {

        @Override
        public void check(UserDetails userDetails) {
            if (!userDetails.isCredentialsNonExpired()) {
                throw new RuntimeException("Account credentials expired");
            }
        }
    }
}
