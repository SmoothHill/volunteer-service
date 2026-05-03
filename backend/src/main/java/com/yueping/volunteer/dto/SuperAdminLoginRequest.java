package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;

public class SuperAdminLoginRequest {

    @NotBlank(message = "超级管理员账号不能为空")
    private String username;

    @NotBlank(message = "超级管理员密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
