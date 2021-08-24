package com.example.shoesmanagement.model.enums;

import lombok.Getter;

@Getter
public enum UserPermission {
    USER_READ("user:read"),
    USER_WRITE("user:write");
    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }
}
