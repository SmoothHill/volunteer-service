package com.yueping.volunteer.service;

import com.yueping.volunteer.model.NotificationChannel;
import com.yueping.volunteer.model.NotificationSendStatus;
import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MockNotificationDispatchService implements NotificationDispatchService {

    private static final Logger log = LoggerFactory.getLogger(MockNotificationDispatchService.class);

    @Override
    public NotificationDispatchResult dispatch(UserProfile user,
                                               NotificationType notificationType,
                                               String title,
                                               String content,
                                               String templateCode) {
        log.info("Mock notification sent: userId={}, type={}, title={}, content={}",
                user == null ? null : user.getId(),
                notificationType,
                title,
                content);
        NotificationDispatchResult result = new NotificationDispatchResult();
        result.setChannel(NotificationChannel.MOCK);
        result.setSendStatus(NotificationSendStatus.SENT);
        result.setProviderMessageId("MOCK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        result.setSentAt(LocalDateTime.now());
        result.setFailureReason("");
        return result;
    }
}
