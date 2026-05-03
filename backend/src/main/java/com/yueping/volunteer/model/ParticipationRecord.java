package com.yueping.volunteer.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "participation_record",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_activity_user", columnNames = {"activityId", "userId"})
        },
        indexes = {
                @Index(name = "idx_record_activity", columnList = "activityId"),
                @Index(name = "idx_record_user", columnList = "userId"),
                @Index(name = "idx_record_status", columnList = "status")
        }
)
public class ParticipationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long activityId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double serviceHours;
    private int earnedPoints;
    private Integer serviceRating;
    private String serviceComment;
    private String pointBreakdown;
    private String certificateNo;
    private String certificateUrl;
    private String evidenceHash;
    private LocalDateTime rewardFinalizedAt;
    private boolean organizerConfirmed;
    private LocalDateTime organizerConfirmedAt;
    private String organizerConfirmComment;
    @Enumerated(EnumType.STRING)
    private AnomalyStatus anomalyStatus;
    private String anomalyRemark;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private CheckInMethod checkInMethod;
    private Double lastLatitude;
    private Double lastLongitude;

    @Transient
    private String activityTitle;

    @Transient
    private String activityCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ParticipationStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public double getServiceHours() {
        return serviceHours;
    }

    public void setServiceHours(double serviceHours) {
        this.serviceHours = serviceHours;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public Integer getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }

    public String getServiceComment() {
        return serviceComment;
    }

    public void setServiceComment(String serviceComment) {
        this.serviceComment = serviceComment;
    }

    public String getPointBreakdown() {
        return pointBreakdown;
    }

    public void setPointBreakdown(String pointBreakdown) {
        this.pointBreakdown = pointBreakdown;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getEvidenceHash() {
        return evidenceHash;
    }

    public void setEvidenceHash(String evidenceHash) {
        this.evidenceHash = evidenceHash;
    }

    public LocalDateTime getRewardFinalizedAt() {
        return rewardFinalizedAt;
    }

    public void setRewardFinalizedAt(LocalDateTime rewardFinalizedAt) {
        this.rewardFinalizedAt = rewardFinalizedAt;
    }

    public boolean isOrganizerConfirmed() {
        return organizerConfirmed;
    }

    public void setOrganizerConfirmed(boolean organizerConfirmed) {
        this.organizerConfirmed = organizerConfirmed;
    }

    public LocalDateTime getOrganizerConfirmedAt() {
        return organizerConfirmedAt;
    }

    public void setOrganizerConfirmedAt(LocalDateTime organizerConfirmedAt) {
        this.organizerConfirmedAt = organizerConfirmedAt;
    }

    public String getOrganizerConfirmComment() {
        return organizerConfirmComment;
    }

    public void setOrganizerConfirmComment(String organizerConfirmComment) {
        this.organizerConfirmComment = organizerConfirmComment;
    }

    public AnomalyStatus getAnomalyStatus() {
        return anomalyStatus;
    }

    public void setAnomalyStatus(AnomalyStatus anomalyStatus) {
        this.anomalyStatus = anomalyStatus;
    }

    public String getAnomalyRemark() {
        return anomalyRemark;
    }

    public void setAnomalyRemark(String anomalyRemark) {
        this.anomalyRemark = anomalyRemark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CheckInMethod getCheckInMethod() {
        return checkInMethod;
    }

    public void setCheckInMethod(CheckInMethod checkInMethod) {
        this.checkInMethod = checkInMethod;
    }

    public Double getLastLatitude() {
        return lastLatitude;
    }

    public void setLastLatitude(Double lastLatitude) {
        this.lastLatitude = lastLatitude;
    }

    public Double getLastLongitude() {
        return lastLongitude;
    }

    public void setLastLongitude(Double lastLongitude) {
        this.lastLongitude = lastLongitude;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }
}
