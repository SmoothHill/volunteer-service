package com.yueping.volunteer.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CreateActivityRequest {

    @NotBlank(message = "活动标题不能为空")
    private String title;

    @NotBlank(message = "活动描述不能为空")
    private String description;

    @NotBlank(message = "活动地点不能为空")
    private String location;

    private String category;

    @Min(value = 1, message = "难度等级不能低于 1")
    @Max(value = 5, message = "难度等级不能高于 5")
    private Integer difficultyLevel;

    @DecimalMin(value = "0.8", message = "难度系数不能低于 0.8")
    @DecimalMax(value = "2.0", message = "难度系数不能高于 2.0")
    private Double difficultyCoefficient;

    @Min(value = 1, message = "需求等级不能低于 1")
    @Max(value = 5, message = "需求等级不能高于 5")
    private Integer demandLevel;

    private Long serviceSiteId;

    @NotNull(message = "纬度不能为空")
    private Double latitude;

    @NotNull(message = "经度不能为空")
    private Double longitude;

    @NotNull(message = "开始时间不能为空")
    @Future(message = "开始时间必须晚于当前时间")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;

    @Min(value = 1, message = "人数上限至少为 1")
    @Max(value = 500, message = "人数上限不能超过 500")
    private int capacity;

    @Min(value = 50, message = "围栏半径不能小于 50 米")
    @Max(value = 2000, message = "围栏半径不能超过 2000 米")
    private int geofenceRadiusMeters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Double getDifficultyCoefficient() {
        return difficultyCoefficient;
    }

    public void setDifficultyCoefficient(Double difficultyCoefficient) {
        this.difficultyCoefficient = difficultyCoefficient;
    }

    public Integer getDemandLevel() {
        return demandLevel;
    }

    public void setDemandLevel(Integer demandLevel) {
        this.demandLevel = demandLevel;
    }

    public Long getServiceSiteId() {
        return serviceSiteId;
    }

    public void setServiceSiteId(Long serviceSiteId) {
        this.serviceSiteId = serviceSiteId;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getGeofenceRadiusMeters() {
        return geofenceRadiusMeters;
    }

    public void setGeofenceRadiusMeters(int geofenceRadiusMeters) {
        this.geofenceRadiusMeters = geofenceRadiusMeters;
    }
}
