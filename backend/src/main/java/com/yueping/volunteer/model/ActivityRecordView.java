package com.yueping.volunteer.model;

import java.time.LocalDateTime;

public class ActivityRecordView {

    private Long recordId;
    private Long activityId;
    private String activityTitle;
    private String activityCategory;
    private Long userId;
    private String userName;
    private ParticipationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double serviceHours;
    private int earnedPoints;
    private Integer serviceRating;
    private String serviceComment;
    private String pointBreakdown;
    private boolean rewardPending;
    private LocalDateTime rewardFinalizedAt;
    private boolean organizerConfirmed;
    private LocalDateTime organizerConfirmedAt;
    private String organizerConfirmComment;
    private String certificateUrl;
    private String evidenceHash;
    private BlockchainProofStatus blockchainProofStatus;
    private LocalDateTime blockchainAnchoredAt;
    private String blockchainTransactionNo;
    private String blockchainProviderName;
    private boolean evidenceValid;
    private int snapshotCount;
    private int trackPointCount;
    private AnomalyStatus anomalyStatus;
    private String anomalyReason;
    private String anomalyRemark;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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

    public ParticipationStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public boolean isRewardPending() {
        return rewardPending;
    }

    public void setRewardPending(boolean rewardPending) {
        this.rewardPending = rewardPending;
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

    public BlockchainProofStatus getBlockchainProofStatus() {
        return blockchainProofStatus;
    }

    public void setBlockchainProofStatus(BlockchainProofStatus blockchainProofStatus) {
        this.blockchainProofStatus = blockchainProofStatus;
    }

    public LocalDateTime getBlockchainAnchoredAt() {
        return blockchainAnchoredAt;
    }

    public void setBlockchainAnchoredAt(LocalDateTime blockchainAnchoredAt) {
        this.blockchainAnchoredAt = blockchainAnchoredAt;
    }

    public String getBlockchainTransactionNo() {
        return blockchainTransactionNo;
    }

    public void setBlockchainTransactionNo(String blockchainTransactionNo) {
        this.blockchainTransactionNo = blockchainTransactionNo;
    }

    public String getBlockchainProviderName() {
        return blockchainProviderName;
    }

    public void setBlockchainProviderName(String blockchainProviderName) {
        this.blockchainProviderName = blockchainProviderName;
    }

    public boolean isEvidenceValid() {
        return evidenceValid;
    }

    public void setEvidenceValid(boolean evidenceValid) {
        this.evidenceValid = evidenceValid;
    }

    public int getSnapshotCount() {
        return snapshotCount;
    }

    public void setSnapshotCount(int snapshotCount) {
        this.snapshotCount = snapshotCount;
    }

    public int getTrackPointCount() {
        return trackPointCount;
    }

    public void setTrackPointCount(int trackPointCount) {
        this.trackPointCount = trackPointCount;
    }

    public AnomalyStatus getAnomalyStatus() {
        return anomalyStatus;
    }

    public void setAnomalyStatus(AnomalyStatus anomalyStatus) {
        this.anomalyStatus = anomalyStatus;
    }

    public String getAnomalyReason() {
        return anomalyReason;
    }

    public void setAnomalyReason(String anomalyReason) {
        this.anomalyReason = anomalyReason;
    }

    public String getAnomalyRemark() {
        return anomalyRemark;
    }

    public void setAnomalyRemark(String anomalyRemark) {
        this.anomalyRemark = anomalyRemark;
    }
}
