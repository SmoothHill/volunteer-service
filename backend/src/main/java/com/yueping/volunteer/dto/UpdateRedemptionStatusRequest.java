package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.RedemptionStatus;

import javax.validation.constraints.NotNull;

public class UpdateRedemptionStatusRequest {

    @NotNull(message = "兑换状态不能为空")
    private RedemptionStatus status;

    private String deliveryRemark;

    public RedemptionStatus getStatus() {
        return status;
    }

    public void setStatus(RedemptionStatus status) {
        this.status = status;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }
}
