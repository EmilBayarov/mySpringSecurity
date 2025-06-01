package org.example.myspringsecurity.securityLib.authentication.auth;

import org.example.myspringsecurity.securityLib.authentication.AuthenticationException;
import org.example.myspringsecurity.securityLib.core.Authentication;

public interface AuthenticationProvider {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;
    boolean supports(Class<?> authentication);
}
