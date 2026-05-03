package com.yueping.volunteer.model;

public class StarLeaderboardView {

    private Long userId;
    private String userName;
    private String avatarUrl;
    private String badgeName;
    private String period;
    private double totalHours;
    private int totalPoints;
    private int completionCount;
    private double positiveRatingRate;
    private double trackCoverageScore;
    private double starScore;
    private String topArea;
    private String highlightTitle;
    private String showcaseText;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getCompletionCount() {
        return completionCount;
    }

    public void setCompletionCount(int completionCount) {
        this.completionCount = completionCount;
    }

    public double getPositiveRatingRate() {
        return positiveRatingRate;
    }

    public void setPositiveRatingRate(double positiveRatingRate) {
        this.positiveRatingRate = positiveRatingRate;
    }

    public double getTrackCoverageScore() {
        return trackCoverageScore;
    }

    public void setTrackCoverageScore(double trackCoverageScore) {
        this.trackCoverageScore = trackCoverageScore;
    }

    public double getStarScore() {
        return starScore;
    }

    public void setStarScore(double starScore) {
        this.starScore = starScore;
    }

    public String getTopArea() {
        return topArea;
    }

    public void setTopArea(String topArea) {
        this.topArea = topArea;
    }

    public String getHighlightTitle() {
        return highlightTitle;
    }

    public void setHighlightTitle(String highlightTitle) {
        this.highlightTitle = highlightTitle;
    }

    public String getShowcaseText() {
        return showcaseText;
    }

    public void setShowcaseText(String showcaseText) {
        this.showcaseText = showcaseText;
    }
}
