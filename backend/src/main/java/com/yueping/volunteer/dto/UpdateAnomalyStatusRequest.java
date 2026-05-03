package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.AnomalyStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateAnomalyStatusRequest {

    @NotNull(message = "\u5904\u7f6e\u72b6\u6001\u4e0d\u80fd\u4e3a\u7a7a")
    private AnomalyStatus status;

    @NotBlank(message = "\u5904\u7f6e\u5907\u6ce8\u4e0d\u80fd\u4e3a\u7a7a")
    private String remark;

    public AnomalyStatus getStatus() {
        return status;
    }

    public void setStatus(AnomalyStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
