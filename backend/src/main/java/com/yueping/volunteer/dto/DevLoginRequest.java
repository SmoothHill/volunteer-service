package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DevLoginRequest {

    @NotBlank(message = "开发登录名称不能为空")
    private String name;

    private String avatarUrl;

    @NotNull(message = "开发登录角色不能为空")
    private UserRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}

