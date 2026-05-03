package com.yueping.volunteer.auth;

public final class AuthContext {

    private static final ThreadLocal<AuthenticatedUser> HOLDER = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void set(AuthenticatedUser user) {
        HOLDER.set(user);
    }

    public static AuthenticatedUser get() {
        return HOLDER.get();
    }

    public static Long getUserId() {
        return HOLDER.get() == null ? null : HOLDER.get().getUserId();
    }

    public static void clear() {
        HOLDER.remove();
    }
}

