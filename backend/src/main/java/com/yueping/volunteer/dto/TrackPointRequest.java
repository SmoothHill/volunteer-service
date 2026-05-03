package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.TrackPointType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TrackPointRequest {

    @NotNull(message = "活动 ID 不能为空")
    private Long activityId;

    @NotNull(message = "纬度不能为空")
    private Double latitude;

    @NotNull(message = "经度不能为空")
    private Double longitude;

    private TrackPointType pointType;
    private LocalDateTime recordedAt;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public TrackPointType getPointType() {
        return pointType;
    }

    public void setPointType(TrackPointType pointType) {
        this.pointType = pointType;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
