package com.yueping.volunteer.service;

import com.yueping.volunteer.model.BlockchainProofStatus;

import java.time.LocalDateTime;

public class BlockchainAnchorResult {

    private BlockchainProofStatus proofStatus;
    private String transactionNo;
    private LocalDateTime anchoredAt;
    private String providerName;
    private String failureReason;

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

    public LocalDateTime getAnchoredAt() {
        return anchoredAt;
    }

    public void setAnchoredAt(LocalDateTime anchoredAt) {
        this.anchoredAt = anchoredAt;
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
