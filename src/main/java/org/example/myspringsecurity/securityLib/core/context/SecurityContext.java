package org.example.myspringsecurity.securityLib.core.context;

import org.example.myspringsecurity.securityLib.core.Authentication;

public interface SecurityContext {
    Authentication getAuthentication();
    void setAuthentication(Authentication authentication);
}
