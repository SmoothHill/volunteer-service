package com.yueping.volunteer.auth;

import com.yueping.volunteer.model.UserRole;

public class AuthenticatedUser {

    private final Long userId;
    private final String openId;
    private final String name;
    private final UserRole role;

    public AuthenticatedUser(Long userId, String openId, String name, UserRole role) {
        this.userId = userId;
        this.openId = openId;
        this.name = name;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getOpenId() {
        return openId;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
}

