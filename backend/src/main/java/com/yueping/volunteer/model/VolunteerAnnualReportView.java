package com.yueping.volunteer.model;

import java.util.ArrayList;
import java.util.List;

public class VolunteerAnnualReportView {

    private int year;
    private double totalServiceHours;
    private int totalActivities;
    private int totalPoints;
    private int totalCertificates;
    private String bestActivityTitle;
    private Integer bestActivityRating;
    private String topCategory;
    private String summaryText;
    private List<String> coveredAreas = new ArrayList<>();
    private List<AnnualFootprintPointView> footprints = new ArrayList<>();
    private List<ActivityRecordView> representativeRecords = new ArrayList<>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTotalServiceHours() {
        return totalServiceHours;
    }

    public void setTotalServiceHours(double totalServiceHours) {
        this.totalServiceHours = totalServiceHours;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalCertificates() {
        return totalCertificates;
    }

    public void setTotalCertificates(int totalCertificates) {
        this.totalCertificates = totalCertificates;
    }

    public String getBestActivityTitle() {
        return bestActivityTitle;
    }

    public void setBestActivityTitle(String bestActivityTitle) {
        this.bestActivityTitle = bestActivityTitle;
    }

    public Integer getBestActivityRating() {
        return bestActivityRating;
    }

    public void setBestActivityRating(Integer bestActivityRating) {
        this.bestActivityRating = bestActivityRating;
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

    public List<String> getCoveredAreas() {
        return coveredAreas;
    }

    public void setCoveredAreas(List<String> coveredAreas) {
        this.coveredAreas = coveredAreas;
    }

    public List<AnnualFootprintPointView> getFootprints() {
        return footprints;
    }

    public void setFootprints(List<AnnualFootprintPointView> footprints) {
        this.footprints = footprints;
    }

    public List<ActivityRecordView> getRepresentativeRecords() {
        return representativeRecords;
    }

    public void setRepresentativeRecords(List<ActivityRecordView> representativeRecords) {
        this.representativeRecords = representativeRecords;
    }
}
