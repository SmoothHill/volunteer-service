package com.yueping.volunteer.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceCertificateView {

    private Long recordId;
    private Long activityId;
    private Long userId;
    private String certificateTitle;
    private String systemName;
    private String volunteerName;
    private String volunteerCardNo;
    private String maskedIdCardNo;
    private String activityTitle;
    private String activityCategory;
    private String activityLocation;
    private LocalDateTime activityStartTime;
    private LocalDateTime activityEndTime;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double serviceHours;
    private Integer serviceRating;
    private int earnedPoints;
    private String serviceComment;
    private LocalDateTime organizerConfirmedAt;
    private String organizerConfirmComment;
    private String certificateNo;
    private String certificateUrl;
    private LocalDateTime rewardFinalizedAt;
    private LocalDateTime issuedAt;
    private String evidenceHash;
    private BlockchainProofStatus blockchainProofStatus;
    private LocalDateTime blockchainAnchoredAt;
    private String blockchainTransactionNo;
    private String blockchainProviderName;
    private List<SnapshotItem> snapshots = new ArrayList<>();

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCertificateTitle() {
        return certificateTitle;
    }

    public void setCertificateTitle(String certificateTitle) {
        this.certificateTitle = certificateTitle;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public String getVolunteerCardNo() {
        return volunteerCardNo;
    }

    public void setVolunteerCardNo(String volunteerCardNo) {
        this.volunteerCardNo = volunteerCardNo;
    }

    public String getMaskedIdCardNo() {
        return maskedIdCardNo;
    }

    public void setMaskedIdCardNo(String maskedIdCardNo) {
        this.maskedIdCardNo = maskedIdCardNo;
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

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public LocalDateTime getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(LocalDateTime activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public LocalDateTime getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(LocalDateTime activityEndTime) {
        this.activityEndTime = activityEndTime;
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

    public Integer getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(Integer serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public String getServiceComment() {
        return serviceComment;
    }

    public void setServiceComment(String serviceComment) {
        this.serviceComment = serviceComment;
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

    public LocalDateTime getRewardFinalizedAt() {
        return rewardFinalizedAt;
    }

    public void setRewardFinalizedAt(LocalDateTime rewardFinalizedAt) {
        this.rewardFinalizedAt = rewardFinalizedAt;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
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

    public List<SnapshotItem> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<SnapshotItem> snapshots) {
        this.snapshots = snapshots;
    }

    public static class SnapshotItem {
        private String imageUrl;
        private String note;
        private LocalDateTime createdAt;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
