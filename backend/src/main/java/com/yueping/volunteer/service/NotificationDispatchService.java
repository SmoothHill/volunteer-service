package com.yueping.volunteer.service;

import com.yueping.volunteer.model.NotificationType;
import com.yueping.volunteer.model.UserProfile;

public interface NotificationDispatchService {

    NotificationDispatchResult dispatch(UserProfile user,
                                        NotificationType notificationType,
                                        String title,
                                        String content,
                                        String templateCode);
}
