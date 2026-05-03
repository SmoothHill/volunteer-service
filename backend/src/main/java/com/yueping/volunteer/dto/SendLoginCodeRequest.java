package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class SendLoginCodeRequest {

    @NotBlank(message = "loginTicket 不能为空")
    private String loginTicket;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式不正确")
    private String phoneNo;

    public String getLoginTicket() {
        return loginTicket;
    }

    public void setLoginTicket(String loginTicket) {
        this.loginTicket = loginTicket;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
