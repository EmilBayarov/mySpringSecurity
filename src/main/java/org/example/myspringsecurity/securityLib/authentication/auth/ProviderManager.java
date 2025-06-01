package org.example.myspringsecurity.securityLib.authentication.auth;

import org.example.myspringsecurity.securityLib.authentication.AuthenticationException;
import org.example.myspringsecurity.securityLib.core.Authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProviderManager implements AuthenticationManager {
    private List<AuthenticationProvider> providers = Collections.emptyList();
    private AuthenticationManager parent;

    public ProviderManager(AuthenticationProvider provider) {
        if (provider == null) {
            throw new IllegalArgumentException("AuthenticationProvider cannot be null");
        }
        if (this.providers.isEmpty()) {
            this.providers = new ArrayList<>();
        }
        this.providers.add(provider);
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Class<? extends Authentication> toTest = authentication.getClass();
        Authentication result = null;
        for (AuthenticationProvider provider : getProviders()) {
            if (!provider.supports(toTest)) {
                continue;
            }
            result = provider.authenticate(authentication);
        }
        if (result == null && this.parent != null) {
            result = parent.authenticate(authentication);
        }
        if (result != null) {
            return result;
        }
        throw new AuthenticationException("Something went wrong");
    }

    private List<AuthenticationProvider> getProviders() {
        return this.providers;
    }
}
