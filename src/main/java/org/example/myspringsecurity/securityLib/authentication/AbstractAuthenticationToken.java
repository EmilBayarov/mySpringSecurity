package org.example.myspringsecurity.securityLib.authentication;

import io.jsonwebtoken.lang.Assert;
import org.example.myspringsecurity.securityLib.core.Authentication;
import org.example.myspringsecurity.securityLib.core.GrantedAuthority;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractAuthenticationToken implements Authentication, Principal {
    private final Collection<GrantedAuthority> authorities;
    private Object details;
    private boolean isAuthenticated = false;

    public AbstractAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) {
            authorities = Collections.emptyList();
        }
        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authority can't be contains null element");
        }
        this.authorities = Collections.unmodifiableList(new ArrayList<>(authorities));
    }


    @Override
    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        if (this.getPrincipal() instanceof Principal principal) {
            return principal.getName();
        }
        return (this.getPrincipal() == null) ? "" : this.getPrincipal().toString();
    }
}
