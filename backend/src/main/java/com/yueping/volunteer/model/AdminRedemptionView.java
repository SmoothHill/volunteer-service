package com.yueping.volunteer.model;

import java.time.LocalDateTime;

public class AdminRedemptionView {

    private Long id;
    private Long userId;
    private String userName;
    private Long itemId;
    private String itemName;
    private int pointsCost;
    private int quantity;
    private StoreItemDeliveryType deliveryType;
    private String recipientName;
    private String maskedRecipientPhone;
    private String recipientAddress;
    private String deliveryRemark;
    private RedemptionStatus status;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPointsCost() {
        return pointsCost;
    }

    public void setPointsCost(int pointsCost) {
        this.pointsCost = pointsCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StoreItemDeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(StoreItemDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getMaskedRecipientPhone() {
        return maskedRecipientPhone;
    }

    public void setMaskedRecipientPhone(String maskedRecipientPhone) {
        this.maskedRecipientPhone = maskedRecipientPhone;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getDeliveryRemark() {
        return deliveryRemark;
    }

    public void setDeliveryRemark(String deliveryRemark) {
        this.deliveryRemark = deliveryRemark;
    }

    public RedemptionStatus getStatus() {
        return status;
    }

    public void setStatus(RedemptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
