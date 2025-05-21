package org.example.myspringsecurity.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissons {
    ADMIN_READ("admin::read"),
    ADMIN_UPDATE("admin::update"),
    ADMIN_CREATE("admin::create"),
    ADMIN_DELETE("admin::delete"),
    MANAGER_READ("MANAGER::read"),
    MANAGER_UPDATE("MANAGER::update"),
    MANAGER_CREATE("MANAGER::create"),
    MANAGER_DELETE("MANAGER::delete")
    ;

    @Getter
    private final String permissions;
}
