package com.yueping.volunteer.service;

import com.yueping.volunteer.model.NotificationChannel;
import com.yueping.volunteer.model.NotificationSendStatus;

import java.time.LocalDateTime;

public class NotificationDispatchResult {

    private NotificationChannel channel;
    private NotificationSendStatus sendStatus;
    private String providerMessageId;
    private String failureReason;
    private LocalDateTime sentAt;

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public NotificationSendStatus getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(NotificationSendStatus sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getProviderMessageId() {
        return providerMessageId;
    }

    public void setProviderMessageId(String providerMessageId) {
        this.providerMessageId = providerMessageId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
