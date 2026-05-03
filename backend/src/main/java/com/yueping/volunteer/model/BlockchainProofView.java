package com.yueping.volunteer.model;

import java.time.LocalDateTime;

public class BlockchainProofView {

    private Long id;
    private Long activityId;
    private Long recordId;
    private Long userId;
    private String userName;
    private BlockchainEventType eventType;
    private BlockchainProofStatus proofStatus;
    private String transactionNo;
    private LocalDateTime eventTime;
    private LocalDateTime anchoredAt;
    private String payloadSummary;
    private String evidenceHash;
    private String providerName;
    private String failureReason;

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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public BlockchainEventType getEventType() {
        return eventType;
    }

    public void setEventType(BlockchainEventType eventType) {
        this.eventType = eventType;
    }

    public BlockchainProofStatus getProofStatus() {
        return proofStatus;
    }

    public void setProofStatus(BlockchainProofStatus proofStatus) {
        this.proofStatus = proofStatus;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public LocalDateTime getAnchoredAt() {
        return anchoredAt;
    }

    public void setAnchoredAt(LocalDateTime anchoredAt) {
        this.anchoredAt = anchoredAt;
    }

    public String getPayloadSummary() {
        return payloadSummary;
    }

    public void setPayloadSummary(String payloadSummary) {
        this.payloadSummary = payloadSummary;
    }

    public String getEvidenceHash() {
        return evidenceHash;
    }

    public void setEvidenceHash(String evidenceHash) {
        this.evidenceHash = evidenceHash;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
