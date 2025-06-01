package org.example.myspringsecurity.core.security.v1;

public class SecurityRule {
    private boolean permitAll = false;
    private boolean authenticated = false;
    private String requiredRole = null;

    public SecurityRule permitAll() {
        this.permitAll = true;
        this.authenticated = false;
        this.requiredRole = null;
        return this;
    }

    public SecurityRule authenticated() {
        this.authenticated = true;
        return this;
    }

    public SecurityRule hasRole(String role) {
        this.requiredRole = role;
        return this;
    }

    public boolean isPermitAll() {
        return permitAll;
    }

    public boolean requiresAuthentication() {
        return authenticated || requiredRole != null;
    }

    public String getRequiredRole() {
        return requiredRole;
    }
}
