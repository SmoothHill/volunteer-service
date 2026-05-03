package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.NotificationMessageRecord;
import com.yueping.volunteer.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationMessageRecordRepository extends JpaRepository<NotificationMessageRecord, Long> {

    List<NotificationMessageRecord> findTop20ByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByNotificationTypeAndUserIdAndBizTypeAndBizId(NotificationType notificationType,
                                                                 Long userId,
                                                                 String bizType,
                                                                 String bizId);
}
