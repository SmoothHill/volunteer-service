package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateSnapshotRequest {

    @NotNull(message = "活动 ID 不能为空")
    private Long activityId;

    @NotBlank(message = "图片地址不能为空")
    private String imageUrl;

    private String note;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

