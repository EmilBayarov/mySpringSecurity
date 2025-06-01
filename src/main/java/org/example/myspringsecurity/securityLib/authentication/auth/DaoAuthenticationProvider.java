package org.example.myspringsecurity.securityLib.authentication.auth;

import org.example.myspringsecurity.securityLib.authentication.AuthenticationException;
import org.example.myspringsecurity.securityLib.authentication.UsernamePasswordAuthenticationToken;
import org.example.myspringsecurity.securityLib.core.Authentication;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetails;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;
import org.example.myspringsecurity.securityLib.crypto.password.PasswordEncoder;

public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        // We can check is password compromised or we should upgrade it
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails user, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new AuthenticationException("Failed to authenticate since no credentials provided");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, user.getPassword())) {
            throw new AuthenticationException("Failed to authenticate since password does not match stored value");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails user = this.userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException("Username not found");
        }
        return user;
    }

    public UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }
}
