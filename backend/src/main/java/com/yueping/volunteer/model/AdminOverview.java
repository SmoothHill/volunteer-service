package com.yueping.volunteer.model;

public class AdminOverview {

    private int totalActivities;
    private int totalVolunteers;
    private int completedRecords;
    private int totalPointsIssued;
    private int pendingSupplements;
    private int pendingAnomalies;
    private int pendingRedemptions;
    private int lowStockItems;

    public AdminOverview() {
    }

    public AdminOverview(int totalActivities, int totalVolunteers, int completedRecords, int totalPointsIssued) {
        this.totalActivities = totalActivities;
        this.totalVolunteers = totalVolunteers;
        this.completedRecords = completedRecords;
        this.totalPointsIssued = totalPointsIssued;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public int getTotalVolunteers() {
        return totalVolunteers;
    }

    public void setTotalVolunteers(int totalVolunteers) {
        this.totalVolunteers = totalVolunteers;
    }

    public int getCompletedRecords() {
        return completedRecords;
    }

    public void setCompletedRecords(int completedRecords) {
        this.completedRecords = completedRecords;
    }

    public int getTotalPointsIssued() {
        return totalPointsIssued;
    }

    public void setTotalPointsIssued(int totalPointsIssued) {
        this.totalPointsIssued = totalPointsIssued;
    }

    public int getPendingSupplements() {
        return pendingSupplements;
    }

    public void setPendingSupplements(int pendingSupplements) {
        this.pendingSupplements = pendingSupplements;
    }

    public int getPendingAnomalies() {
        return pendingAnomalies;
    }

    public void setPendingAnomalies(int pendingAnomalies) {
        this.pendingAnomalies = pendingAnomalies;
    }

    public int getPendingRedemptions() {
        return pendingRedemptions;
    }

    public void setPendingRedemptions(int pendingRedemptions) {
        this.pendingRedemptions = pendingRedemptions;
    }

    public int getLowStockItems() {
        return lowStockItems;
    }

    public void setLowStockItems(int lowStockItems) {
        this.lowStockItems = lowStockItems;
    }
}
