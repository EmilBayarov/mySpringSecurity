package org.example.myspringsecurity.securityLib.authentication.builders;

import org.example.myspringsecurity.securityLib.authentication.auth.AuthenticationManager;
import org.example.myspringsecurity.securityLib.authentication.auth.AuthenticationProvider;
import org.example.myspringsecurity.securityLib.core.userDetails.UserDetailsService;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManagerBuilder {
    private AuthenticationManager authenticationManager;
    private List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
    private UserDetailsService userDetailsService;

    public AuthenticationManagerBuilder authenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProviders.add(authenticationProvider);
        return this;
    }
}
