package com.yueping.volunteer.model;

import java.util.ArrayList;
import java.util.List;

public class AdminAnnualReportView {

    private int year;
    private Long adminUserId;
    private String adminName;
    private int totalActivities;
    private int totalServiceCount;
    private double totalServiceHours;
    private int coveredCommunityCount;
    private int activeMonths;
    private String hottestCommunity;
    private String topCategory;
    private String summaryText;
    private List<AreaStatView> coveredCommunities = new ArrayList<>();
    private List<AreaStatView> categoryStats = new ArrayList<>();
    private List<TrendStatView> monthlyActivityTrends = new ArrayList<>();
    private List<TrendStatView> monthlyServiceHourTrends = new ArrayList<>();
    private List<HeatmapPointView> communityHeatmap = new ArrayList<>();
    private List<HeatmapPointView> activityHeatmap = new ArrayList<>();
    private List<ActivityRecordView> representativeRecords = new ArrayList<>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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

    public int getActiveMonths() {
        return activeMonths;
    }

    public void setActiveMonths(int activeMonths) {
        this.activeMonths = activeMonths;
    }

    public String getHottestCommunity() {
        return hottestCommunity;
    }

    public void setHottestCommunity(String hottestCommunity) {
        this.hottestCommunity = hottestCommunity;
    }

    public String getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(String topCategory) {
        this.topCategory = topCategory;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public List<AreaStatView> getCoveredCommunities() {
        return coveredCommunities;
    }

    public void setCoveredCommunities(List<AreaStatView> coveredCommunities) {
        this.coveredCommunities = coveredCommunities;
    }

    public List<AreaStatView> getCategoryStats() {
        return categoryStats;
    }

    public void setCategoryStats(List<AreaStatView> categoryStats) {
        this.categoryStats = categoryStats;
    }

    public List<TrendStatView> getMonthlyActivityTrends() {
        return monthlyActivityTrends;
    }

    public void setMonthlyActivityTrends(List<TrendStatView> monthlyActivityTrends) {
        this.monthlyActivityTrends = monthlyActivityTrends;
    }

    public List<TrendStatView> getMonthlyServiceHourTrends() {
        return monthlyServiceHourTrends;
    }

    public void setMonthlyServiceHourTrends(List<TrendStatView> monthlyServiceHourTrends) {
        this.monthlyServiceHourTrends = monthlyServiceHourTrends;
    }

    public List<HeatmapPointView> getCommunityHeatmap() {
        return communityHeatmap;
    }

    public void setCommunityHeatmap(List<HeatmapPointView> communityHeatmap) {
        this.communityHeatmap = communityHeatmap;
    }

    public List<HeatmapPointView> getActivityHeatmap() {
        return activityHeatmap;
    }

    public void setActivityHeatmap(List<HeatmapPointView> activityHeatmap) {
        this.activityHeatmap = activityHeatmap;
    }

    public List<ActivityRecordView> getRepresentativeRecords() {
        return representativeRecords;
    }

    public void setRepresentativeRecords(List<ActivityRecordView> representativeRecords) {
        this.representativeRecords = representativeRecords;
    }
}
