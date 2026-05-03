package com.yueping.volunteer.auth;

import com.yueping.volunteer.model.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {

    UserRole[] roles() default {};

    boolean allowUnassigned() default false;
}
