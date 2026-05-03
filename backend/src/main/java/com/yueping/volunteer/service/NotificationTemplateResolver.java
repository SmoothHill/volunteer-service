package com.yueping.volunteer.service;

import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.properties.NotificationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationTemplateResolver {

    private final NotificationProperties notificationProperties;

    public NotificationTemplateResolver(NotificationProperties notificationProperties) {
        this.notificationProperties = notificationProperties;
    }

    public Optional<String> resolveTemplateId(NotificationType notificationType) {
        String templateId = notificationProperties.getTemplateIds().get(notificationType.name());
        return StringUtils.hasText(templateId) ? Optional.of(templateId.trim()) : Optional.empty();
    }

    public String resolvePagePath(NotificationType notificationType) {
        String pagePath = notificationProperties.getPagePaths().get(notificationType.name());
        if (StringUtils.hasText(pagePath)) {
            return pagePath.trim();
        }
        if (isAdminNotification(notificationType)) {
            return "/pages/admin/admin";
        }
        return "/pages/messages/messages";
    }

    public List<String> resolveTemplateDataKeys(NotificationType notificationType) {
        List<String> keys = notificationProperties.getTemplateDataKeys().get(notificationType.name());
        return keys == null ? Collections.emptyList() : keys;
    }

    public boolean supportsWechat(NotificationType notificationType) {
        return resolveTemplateId(notificationType).isPresent();
    }

    private boolean isAdminNotification(NotificationType notificationType) {
        return notificationType == NotificationType.ENROLLMENT_PENDING_REVIEW
                || notificationType == NotificationType.SUPPLEMENT_PENDING_REVIEW
                || notificationType == NotificationType.SERVICE_CONFIRM_PENDING;
    }
}
