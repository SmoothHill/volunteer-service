package com.yueping.volunteer.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "blockchain_proof_record",
        indexes = {
                @Index(name = "idx_proof_activity", columnList = "activityId"),
                @Index(name = "idx_proof_record", columnList = "recordId"),
                @Index(name = "idx_proof_user", columnList = "userId"),
                @Index(name = "idx_proof_event_time", columnList = "eventTime")
        }
)
public class BlockchainProofRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long activityId;
    private Long recordId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private BlockchainEventType eventType;
    private String evidenceHash;
    @Enumerated(EnumType.STRING)
    private BlockchainProofStatus proofStatus;
    private String transactionNo;
    private LocalDateTime eventTime;
    private LocalDateTime anchoredAt;
    private String payloadSummary;
    private String providerName;
    private String failureReason;
    private LocalDateTime createdAt;

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

    public BlockchainEventType getEventType() {
        return eventType;
    }

    public void setEventType(BlockchainEventType eventType) {
        this.eventType = eventType;
    }

    public String getEvidenceHash() {
        return evidenceHash;
    }

    public void setEvidenceHash(String evidenceHash) {
        this.evidenceHash = evidenceHash;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
