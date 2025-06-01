package org.example.myspringsecurity.securityLib.authentication.auth;

import org.example.myspringsecurity.securityLib.authentication.AuthenticationException;
import org.example.myspringsecurity.securityLib.core.Authentication;

public interface AuthenticationManager {
    Authentication authenticate(Authentication authentication) throws AuthenticationException;

}
