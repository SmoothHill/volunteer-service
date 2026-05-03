package com.yueping.volunteer.model;

public enum UserRole {
    VOLUNTEER,
    ADMIN,
    SUPER_ADMIN;

    public boolean canAccess(UserRole requiredRole) {
        if (this == requiredRole) {
            return true;
        }
        return this == SUPER_ADMIN && requiredRole == ADMIN;
    }
}
