package com.yueping.volunteer.service;

import com.yueping.volunteer.dto.SaveNotificationSubscriptionItem;
import com.yueping.volunteer.dto.SaveNotificationSubscriptionsRequest;
import com.yueping.volunteer.model.NotificationSubscriptionPreference;
import com.yueping.volunteer.model.NotificationSubscriptionStatus;
import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.repository.NotificationSubscriptionPreferenceRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationSubscriptionService {

    private final NotificationSubscriptionPreferenceRepository preferenceRepository;
    private final UserProfileRepository userProfileRepository;

    public NotificationSubscriptionService(NotificationSubscriptionPreferenceRepository preferenceRepository,
                                           UserProfileRepository userProfileRepository) {
        this.preferenceRepository = preferenceRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void saveSubscriptions(Long userId, SaveNotificationSubscriptionsRequest request) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("当前用户不存在"));
        List<SaveNotificationSubscriptionItem> items = request.getItems();
        LocalDateTime now = LocalDateTime.now();
        for (SaveNotificationSubscriptionItem item : items) {
            NotificationType notificationType = NotificationType.valueOf(item.getNotificationType().trim());
            NotificationSubscriptionStatus status = NotificationSubscriptionStatus.valueOf(item.getAcceptStatus().trim().toUpperCase());
            NotificationSubscriptionPreference preference = preferenceRepository
                    .findByUserIdAndNotificationType(userId, notificationType)
                    .orElseGet(NotificationSubscriptionPreference::new);
            preference.setUserId(userId);
            preference.setOpenId(user.getOpenId());
            preference.setNotificationType(notificationType);
            preference.setTemplateCode(StringUtils.hasText(item.getTemplateCode()) ? item.getTemplateCode().trim() : "");
            preference.setAcceptStatus(status);
            preference.setUpdatedAt(now);
            if (status == NotificationSubscriptionStatus.ACCEPT) {
                preference.setLastAcceptedAt(now);
            } else {
                preference.setLastRejectedAt(now);
            }
            preferenceRepository.save(preference);
        }
    }

    @Transactional(readOnly = true)
    public boolean canSendWechat(Long userId, NotificationType notificationType, String templateCode) {
        if (userId == null || !StringUtils.hasText(templateCode)) {
            return false;
        }
        return preferenceRepository.findByUserIdAndNotificationType(userId, notificationType)
                .filter(preference -> templateCode.equals(preference.getTemplateCode()))
                .map(preference -> preference.getAcceptStatus() == NotificationSubscriptionStatus.ACCEPT)
                .orElse(false);
    }
}
