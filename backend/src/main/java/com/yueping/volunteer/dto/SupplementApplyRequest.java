package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.SupplementType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class SupplementApplyRequest {

    @NotNull(message = "活动 ID 不能为空")
    private Long activityId;

    @NotNull(message = "补签类型不能为空")
    private SupplementType type;

    @NotNull(message = "申请时间不能为空")
    private LocalDateTime requestedTime;

    @NotBlank(message = "补签原因不能为空")
    private String reason;

    @NotBlank(message = "补签凭证图片不能为空")
    private String evidenceImageUrl;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public SupplementType getType() {
        return type;
    }

    public void setType(SupplementType type) {
        this.type = type;
    }

    public LocalDateTime getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalDateTime requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEvidenceImageUrl() {
        return evidenceImageUrl;
    }

    public void setEvidenceImageUrl(String evidenceImageUrl) {
        this.evidenceImageUrl = evidenceImageUrl;
    }
}

