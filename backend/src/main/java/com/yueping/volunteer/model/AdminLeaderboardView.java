package com.yueping.volunteer.model;

public class AdminLeaderboardView {

    private Long userId;
    private String userName;
    private int totalActivities;
    private int totalServiceCount;
    private double totalServiceHours;
    private int coveredCommunityCount;

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

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public int getTotalServiceCount() {
        return totalServiceCount;
    }

    public void setTotalServiceCount(int totalServiceCount) {
        this.totalServiceCount = totalServiceCount;
    }

    public double getTotalServiceHours() {
        return totalServiceHours;
    }

    public void setTotalServiceHours(double totalServiceHours) {
        this.totalServiceHours = totalServiceHours;
    }

    public int getCoveredCommunityCount() {
        return coveredCommunityCount;
    }

    public void setCoveredCommunityCount(int coveredCommunityCount) {
        this.coveredCommunityCount = coveredCommunityCount;
    }
}
