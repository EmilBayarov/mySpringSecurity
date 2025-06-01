package org.example.myspringsecurity.securityLib.core.userDetails;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username);
}
