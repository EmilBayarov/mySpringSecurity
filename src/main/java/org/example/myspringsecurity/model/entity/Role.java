package org.example.myspringsecurity.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.myspringsecurity.securityLib.core.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    Permissons.ADMIN_CREATE,Permissons.ADMIN_READ,Permissons.ADMIN_UPDATE, Permissons.ADMIN_DELETE
            )
    ),
    MANAGER(
            Set.of(Permissons.MANAGER_CREATE, Permissons.MANAGER_READ, Permissons.MANAGER_DELETE, Permissons.MANAGER_UPDATE)
    )
    ;
    @Getter
    private final Set<Permissons> permissons;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissons()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
