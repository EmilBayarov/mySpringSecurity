package org.example.myspringsecurity.securityLib.core;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {
    String getAuthorities();
}
