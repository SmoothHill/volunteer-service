package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.StoreItemDeliveryType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StoreItemManageRequest {

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "商品分类不能为空")
    private String category;

    @NotBlank(message = "商品描述不能为空")
    private String description;

    @Min(value = 0, message = "所需积分不能小于 0")
    private int pointsCost;

    @Min(value = 0, message = "库存不能小于 0")
    private int stock;

    private boolean active;

    @NotNull(message = "商品类型不能为空")
    private StoreItemDeliveryType deliveryType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointsCost() {
        return pointsCost;
    }

    public void setPointsCost(int pointsCost) {
        this.pointsCost = pointsCost;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public StoreItemDeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(StoreItemDeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }
}
