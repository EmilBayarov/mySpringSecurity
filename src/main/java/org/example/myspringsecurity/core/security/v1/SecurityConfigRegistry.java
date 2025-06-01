package org.example.myspringsecurity.core.security.v1;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SecurityConfigRegistry {
    private final Map<String, SecurityRule> rules = new LinkedHashMap<>();
    public SecurityConfigRegistry() {
        rules.put("/demo/test/**", new SecurityRule().permitAll());
        rules.put("/swagger-ui/**", new SecurityRule().permitAll());
        rules.put("/v3/api-docs/**", new SecurityRule().permitAll());
        rules.put("/auth/authenticate/**", new SecurityRule().permitAll());
        rules.put("/auth/test/**", new SecurityRule().permitAll());
        rules.put("/auth/register/**", new SecurityRule().permitAll());
        rules.put("/auth/confirm/**", new SecurityRule().permitAll());


        rules.put("/auth/logout/**", new SecurityRule().authenticated());
        rules.put("/auth/all/**", new SecurityRule().authenticated());
        rules.put("/auth/refresh/**", new SecurityRule().authenticated());

        rules.put("/demo/test/**", new SecurityRule().hasRole("ROLE_ADMIN").authenticated());

        rules.put("/api/editor/**", new SecurityRule().hasRole("ROLE_ADMIN").authenticated());

    }

    public Optional<SecurityRule> getRuleForPath(String path) {
        for (Map.Entry<String, SecurityRule> entry : rules.entrySet()) {
            if (pathMatches(entry.getKey(), path)) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    private boolean pathMatches(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            return path.startsWith(pattern.substring(0, pattern.length() - 3));
        }
        return pattern.equals(path);
    }
}
