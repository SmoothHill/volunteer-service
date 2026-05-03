package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;

public class AdminApplicationRequest {

    @NotBlank(message = "申请原因不能为空")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
