package com.yueping.volunteer.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminUserAnalyticsView {

    private Long userId;
    private String userName;
    private String avatarUrl;
    private UserRole role;
    private Boolean verified;
    private String realName;
    private String maskedIdCardNo;
    private String volunteerCardNo;
    private String badgeName;
    private double totalHours;
    private int totalPoints;
    private int creditScore;
    private LocalDateTime createdAt;
    private Integer leaderboardRank;
    private VolunteerAnnualReportView annualReport;
    private List<ParticipationRecord> participationRecords = new ArrayList<>();
    private List<HeatmapPointView> relatedHeatmapActivities = new ArrayList<>();
    private UserEvidenceSummaryView evidenceSummary;

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMaskedIdCardNo() {
        return maskedIdCardNo;
    }

    public void setMaskedIdCardNo(String maskedIdCardNo) {
        this.maskedIdCardNo = maskedIdCardNo;
    }

    public String getVolunteerCardNo() {
        return volunteerCardNo;
    }

    public void setVolunteerCardNo(String volunteerCardNo) {
        this.volunteerCardNo = volunteerCardNo;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
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

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLeaderboardRank() {
        return leaderboardRank;
    }

    public void setLeaderboardRank(Integer leaderboardRank) {
        this.leaderboardRank = leaderboardRank;
    }

    public VolunteerAnnualReportView getAnnualReport() {
        return annualReport;
    }

    public void setAnnualReport(VolunteerAnnualReportView annualReport) {
        this.annualReport = annualReport;
    }

    public List<ParticipationRecord> getParticipationRecords() {
        return participationRecords;
    }

    public void setParticipationRecords(List<ParticipationRecord> participationRecords) {
        this.participationRecords = participationRecords;
    }

    public List<HeatmapPointView> getRelatedHeatmapActivities() {
        return relatedHeatmapActivities;
    }

    public void setRelatedHeatmapActivities(List<HeatmapPointView> relatedHeatmapActivities) {
        this.relatedHeatmapActivities = relatedHeatmapActivities;
    }

    public UserEvidenceSummaryView getEvidenceSummary() {
        return evidenceSummary;
    }

    public void setEvidenceSummary(UserEvidenceSummaryView evidenceSummary) {
        this.evidenceSummary = evidenceSummary;
    }
}
