package com.yueping.volunteer.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateSystemSettingsRequest {

    @NotBlank(message = "系统名称不能为空")
    private String systemName;

    @NotNull(message = "默认围栏半径不能为空")
    @Min(value = 1, message = "默认围栏半径必须大于 0")
    private Integer defaultGeofenceRadiusMeters;

    @NotNull(message = "最少轨迹点数不能为空")
    @Min(value = 1, message = "最少轨迹点数必须大于 0")
    private Integer minTrackPoints;

    @NotNull(message = "最少快照数量不能为空")
    @Min(value = 0, message = "最少快照数量不能小于 0")
    private Integer minSnapshotCount;

    @NotNull(message = "最短服务时长不能为空")
    @DecimalMin(value = "0.1", message = "最短服务时长不能小于 0.1 小时")
    private Double minServiceHours;

    @NotNull(message = "30天补签阈值不能为空")
    @Min(value = 1, message = "30天补签阈值必须大于 0")
    private Integer frequentSupplementLimit30Days;

    @NotBlank(message = "证书标题不能为空")
    private String certificateTitle;

    @NotBlank(message = "补签审核默认意见不能为空")
    private String supplementApproveComment;

    private String adminWebUrl;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public Integer getDefaultGeofenceRadiusMeters() {
        return defaultGeofenceRadiusMeters;
    }

    public void setDefaultGeofenceRadiusMeters(Integer defaultGeofenceRadiusMeters) {
        this.defaultGeofenceRadiusMeters = defaultGeofenceRadiusMeters;
    }

    public int getMinTrackPoints() {
        return minTrackPoints;
    }

    public void setMinTrackPoints(int minTrackPoints) {
        this.minTrackPoints = minTrackPoints;
    }

    public int getMinSnapshotCount() {
        return minSnapshotCount;
    }

    public void setMinSnapshotCount(int minSnapshotCount) {
        this.minSnapshotCount = minSnapshotCount;
    }

    public double getMinServiceHours() {
        return minServiceHours;
    }

    public void setMinServiceHours(double minServiceHours) {
        this.minServiceHours = minServiceHours;
    }

    public int getFrequentSupplementLimit30Days() {
        return frequentSupplementLimit30Days;
    }

    public void setFrequentSupplementLimit30Days(int frequentSupplementLimit30Days) {
        this.frequentSupplementLimit30Days = frequentSupplementLimit30Days;
    }

    public String getCertificateTitle() {
        return certificateTitle;
    }

    public void setCertificateTitle(String certificateTitle) {
        this.certificateTitle = certificateTitle;
    }

    public String getSupplementApproveComment() {
        return supplementApproveComment;
    }

    public void setSupplementApproveComment(String supplementApproveComment) {
        this.supplementApproveComment = supplementApproveComment;
    }

    public String getAdminWebUrl() {
        return adminWebUrl;
    }

    public void setAdminWebUrl(String adminWebUrl) {
        this.adminWebUrl = adminWebUrl;
    }
}
