package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.NotificationSubscriptionPreference;
import com.yueping.volunteer.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSubscriptionPreferenceRepository extends JpaRepository<NotificationSubscriptionPreference, Long> {

    Optional<NotificationSubscriptionPreference> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}
