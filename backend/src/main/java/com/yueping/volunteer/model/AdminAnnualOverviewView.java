package com.yueping.volunteer.model;

import java.util.ArrayList;
import java.util.List;

public class AdminAnnualOverviewView {

    private int year;
    private int totalActivities;
    private double totalServiceHours;
    private int activeVolunteers;
    private List<AreaStatView> hotAreas = new ArrayList<>();

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

    public List<AreaStatView> getHotAreas() {
        return hotAreas;
    }

    public void setHotAreas(List<AreaStatView> hotAreas) {
        this.hotAreas = hotAreas;
    }
}
