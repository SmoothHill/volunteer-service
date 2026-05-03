package com.yueping.volunteer.model;

public class SystemSettingsView {

    private String systemName;
    private int defaultGeofenceRadiusMeters;
    private int minTrackPoints;
    private int minSnapshotCount;
    private double minServiceHours;
    private int frequentSupplementLimit30Days;
    private String certificateTitle;
    private String supplementApproveComment;
    private String adminWebUrl;

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public int getDefaultGeofenceRadiusMeters() {
        return defaultGeofenceRadiusMeters;
    }

    public void setDefaultGeofenceRadiusMeters(int defaultGeofenceRadiusMeters) {
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
