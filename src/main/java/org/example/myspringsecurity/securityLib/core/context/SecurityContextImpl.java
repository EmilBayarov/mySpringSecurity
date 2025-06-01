package org.example.myspringsecurity.securityLib.core.context;

import org.example.myspringsecurity.securityLib.core.Authentication;

public class SecurityContextImpl implements SecurityContext {
    private Authentication authentication;

    public SecurityContextImpl() {
    }

    public SecurityContextImpl(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public Authentication getAuthentication() {
        return this.authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
