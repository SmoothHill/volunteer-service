package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.UserRole;

import javax.validation.constraints.NotNull;

public class UpdateUserRoleRequest {

    @NotNull(message = "角色不能为空")
    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
