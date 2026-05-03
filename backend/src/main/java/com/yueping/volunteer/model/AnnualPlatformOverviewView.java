package com.yueping.volunteer.model;

import java.util.ArrayList;
import java.util.List;

public class AnnualPlatformOverviewView {

    private int year;
    private int totalActivities;
    private double totalServiceHours;
    private int activeVolunteers;
    private int activeAdmins;
    private int totalServiceCount;
    private List<AreaStatView> hotAreas = new ArrayList<>();
    private List<AreaStatView> categoryStats = new ArrayList<>();
    private List<TrendStatView> monthlyActivityTrends = new ArrayList<>();
    private List<TrendStatView> monthlyServiceHourTrends = new ArrayList<>();
    private List<HeatmapPointView> communityHeatmap = new ArrayList<>();
    private List<HeatmapPointView> activityHeatmap = new ArrayList<>();
    private List<AdminLeaderboardView> adminLeaderboard = new ArrayList<>();
    private List<LeaderboardView> volunteerLeaderboard = new ArrayList<>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public double getTotalServiceHours() {
        return totalServiceHours;
    }

    public void setTotalServiceHours(double totalServiceHours) {
        this.totalServiceHours = totalServiceHours;
    }

    public int getActiveVolunteers() {
        return activeVolunteers;
    }

    public void setActiveVolunteers(int activeVolunteers) {
        this.activeVolunteers = activeVolunteers;
    }

    public int getActiveAdmins() {
        return activeAdmins;
    }

    public void setActiveAdmins(int activeAdmins) {
        this.activeAdmins = activeAdmins;
    }

    public int getTotalServiceCount() {
        return totalServiceCount;
    }

    public void setTotalServiceCount(int totalServiceCount) {
        this.totalServiceCount = totalServiceCount;
    }

    public List<AreaStatView> getHotAreas() {
        return hotAreas;
    }

    public void setHotAreas(List<AreaStatView> hotAreas) {
        this.hotAreas = hotAreas;
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

    public List<HeatmapPointView> getActivityHeatmap() {
        return activityHeatmap;
    }

    public List<HeatmapPointView> getCommunityHeatmap() {
        return communityHeatmap;
    }

    public void setCommunityHeatmap(List<HeatmapPointView> communityHeatmap) {
        this.communityHeatmap = communityHeatmap;
    }

    public void setActivityHeatmap(List<HeatmapPointView> activityHeatmap) {
        this.activityHeatmap = activityHeatmap;
    }

    public List<AdminLeaderboardView> getAdminLeaderboard() {
        return adminLeaderboard;
    }

    public void setAdminLeaderboard(List<AdminLeaderboardView> adminLeaderboard) {
        this.adminLeaderboard = adminLeaderboard;
    }

    public List<LeaderboardView> getVolunteerLeaderboard() {
        return volunteerLeaderboard;
    }

    public void setVolunteerLeaderboard(List<LeaderboardView> volunteerLeaderboard) {
        this.volunteerLeaderboard = volunteerLeaderboard;
    }
}
