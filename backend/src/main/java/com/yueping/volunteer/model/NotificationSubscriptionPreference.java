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
        name = "notification_subscription_preference",
        indexes = {
                @Index(name = "idx_notification_sub_user_type", columnList = "userId,notificationType", unique = true)
        }
)
public class NotificationSubscriptionPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String openId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String templateCode;

    @Enumerated(EnumType.STRING)
    private NotificationSubscriptionStatus acceptStatus;

    private LocalDateTime lastAcceptedAt;
    private LocalDateTime lastRejectedAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public NotificationSubscriptionStatus getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(NotificationSubscriptionStatus acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public LocalDateTime getLastAcceptedAt() {
        return lastAcceptedAt;
    }

    public void setLastAcceptedAt(LocalDateTime lastAcceptedAt) {
        this.lastAcceptedAt = lastAcceptedAt;
    }

    public LocalDateTime getLastRejectedAt() {
        return lastRejectedAt;
    }

    public void setLastRejectedAt(LocalDateTime lastRejectedAt) {
        this.lastRejectedAt = lastRejectedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
