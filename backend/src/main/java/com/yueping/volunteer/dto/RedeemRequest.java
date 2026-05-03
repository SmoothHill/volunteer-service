package com.yueping.volunteer.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RedeemRequest {

    @NotNull(message = "商品 ID 不能为空")
    private Long itemId;

    @Min(value = 1, message = "兑换数量至少为 1")
    private int quantity;

    private String recipientName;
    private String recipientPhone;
    private String recipientAddress;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }
}
