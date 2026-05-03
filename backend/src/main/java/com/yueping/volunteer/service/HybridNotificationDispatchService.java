package com.yueping.volunteer.service;

import com.yueping.volunteer.model.NotificationChannel;
import com.yueping.volunteer.model.NotificationSendStatus;
import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.properties.NotificationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Primary
@Service
public class HybridNotificationDispatchService implements NotificationDispatchService {

    private static final Logger log = LoggerFactory.getLogger(HybridNotificationDispatchService.class);

    private final NotificationProperties notificationProperties;
    private final NotificationTemplateResolver templateResolver;
    private final NotificationSubscriptionService notificationSubscriptionService;
    private final MockNotificationDispatchService mockNotificationDispatchService;
    private final WechatNotificationDispatchService wechatNotificationDispatchService;

    public HybridNotificationDispatchService(NotificationProperties notificationProperties,
                                             NotificationTemplateResolver templateResolver,
                                             NotificationSubscriptionService notificationSubscriptionService,
                                             MockNotificationDispatchService mockNotificationDispatchService,
                                             WechatNotificationDispatchService wechatNotificationDispatchService) {
        this.notificationProperties = notificationProperties;
        this.templateResolver = templateResolver;
        this.notificationSubscriptionService = notificationSubscriptionService;
        this.mockNotificationDispatchService = mockNotificationDispatchService;
        this.wechatNotificationDispatchService = wechatNotificationDispatchService;
    }

    @Override
    public NotificationDispatchResult dispatch(UserProfile user,
                                               NotificationType notificationType,
                                               String title,
                                               String content,
                                               String templateCode) {
        String mode = notificationProperties.getDispatchMode();
        if ("MOCK".equalsIgnoreCase(mode)) {
            return mockNotificationDispatchService.dispatch(user, notificationType, title, content, templateCode);
        }

        if (!templateResolver.supportsWechat(notificationType)
                || !StringUtils.hasText(templateCode)
                || user == null
                || !StringUtils.hasText(user.getOpenId())
                || !notificationSubscriptionService.canSendWechat(user.getId(), notificationType, templateCode)) {
            return buildInAppResult();
        }

        NotificationDispatchResult wechatResult = wechatNotificationDispatchService.dispatch(
                user,
                notificationType,
                title,
                content,
                templateCode
        );
        if ("WECHAT".equalsIgnoreCase(mode)) {
            return wechatResult;
        }
        if (wechatResult.getSendStatus() == NotificationSendStatus.SENT) {
            return wechatResult;
        }
        log.info("Wechat notification skipped or failed, fallback to站内消息: userId={}, type={}, reason={}",
                user.getId(),
                notificationType,
                wechatResult.getFailureReason());
        return buildInAppResult();
    }

    private NotificationDispatchResult buildInAppResult() {
        NotificationDispatchResult result = new NotificationDispatchResult();
        result.setChannel(NotificationChannel.IN_APP);
        result.setSendStatus(NotificationSendStatus.SENT);
        result.setFailureReason("");
        result.setSentAt(LocalDateTime.now());
        return result;
    }
}
