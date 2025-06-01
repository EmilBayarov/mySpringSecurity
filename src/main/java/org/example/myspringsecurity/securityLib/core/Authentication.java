package org.example.myspringsecurity.securityLib.core;

import java.security.Principal;
import java.util.Collection;

public interface Authentication extends Principal {
    Collection<? extends GrantedAuthority> getAuthorities();
    Object getPrincipal();
    Object getCredentials();
    Object getDetails();
    boolean isAuthenticated();
    void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
}
