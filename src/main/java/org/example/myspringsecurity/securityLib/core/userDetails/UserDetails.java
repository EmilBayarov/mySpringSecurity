package org.example.myspringsecurity.securityLib.core.userDetails;

import org.example.myspringsecurity.securityLib.core.GrantedAuthority;

import java.util.Collection;

public interface UserDetails {
    Collection<? extends GrantedAuthority> getAuthorities();
    String getUsername();
    String getPassword();
    default boolean isAccountNonExpired() {
        return true;
    }
    default boolean isAccountNonLocked() {
        return true;
    }
    default boolean isEnabled() {
        return true;
    }
    default boolean isCredentialsNonExpired() {
        return true;
    }
}
