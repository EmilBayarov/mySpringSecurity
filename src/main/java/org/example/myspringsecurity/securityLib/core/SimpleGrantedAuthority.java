package org.example.myspringsecurity.securityLib.core;

public final class SimpleGrantedAuthority implements GrantedAuthority {
    private final String role;
    public SimpleGrantedAuthority(String role) {
        this.role = role;
    }
    @Override
    public String getAuthorities() {
        return role;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof SimpleGrantedAuthority sga) {
            return this.role.equals(sga.getAuthorities());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
