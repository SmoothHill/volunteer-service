package com.yueping.volunteer.model;

public class CommunityInsightView {

    private String communityName;
    private String streetName;
    private String location;
    private double latitude;
    private double longitude;
    private int activityCount;
    private int totalEnrollments;
    private int gapCount;
    private double trackCoverageScore;
    private int demandIndex;
    private int vitalityIndex;
    private String areaTag;
    private String guidanceText;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public int getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(int totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public int getGapCount() {
        return gapCount;
    }

    public void setGapCount(int gapCount) {
        this.gapCount = gapCount;
    }

    public double getTrackCoverageScore() {
        return trackCoverageScore;
    }

    public void setTrackCoverageScore(double trackCoverageScore) {
        this.trackCoverageScore = trackCoverageScore;
    }

    public int getDemandIndex() {
        return demandIndex;
    }

    public void setDemandIndex(int demandIndex) {
        this.demandIndex = demandIndex;
    }

    public int getVitalityIndex() {
        return vitalityIndex;
    }

    public void setVitalityIndex(int vitalityIndex) {
        this.vitalityIndex = vitalityIndex;
    }

    public String getAreaTag() {
        return areaTag;
    }

    public void setAreaTag(String areaTag) {
        this.areaTag = areaTag;
    }

    public String getGuidanceText() {
        return guidanceText;
    }

    public void setGuidanceText(String guidanceText) {
        this.guidanceText = guidanceText;
    }
}
