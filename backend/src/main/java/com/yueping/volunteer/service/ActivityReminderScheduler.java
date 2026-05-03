package com.yueping.volunteer.service;

import com.yueping.volunteer.model.ActivityStatus;
import com.yueping.volunteer.model.ParticipationRecord;
import com.yueping.volunteer.model.ParticipationStatus;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.VolunteerActivity;
import com.yueping.volunteer.repository.ParticipationRecordRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import com.yueping.volunteer.repository.VolunteerActivityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ActivityReminderScheduler {

    private final VolunteerActivityRepository volunteerActivityRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final UserProfileRepository userProfileRepository;
    private final NotificationService notificationService;
    private final int activityReminderMinutes;

    public ActivityReminderScheduler(VolunteerActivityRepository volunteerActivityRepository,
                                     ParticipationRecordRepository participationRecordRepository,
                                     UserProfileRepository userProfileRepository,
                                     NotificationService notificationService,
                                     @Value("${app.notification.activity-reminder-minutes:60}") int activityReminderMinutes) {
        this.volunteerActivityRepository = volunteerActivityRepository;
        this.participationRecordRepository = participationRecordRepository;
        this.userProfileRepository = userProfileRepository;
        this.notificationService = notificationService;
        this.activityReminderMinutes = activityReminderMinutes;
    }

    @Scheduled(fixedDelayString = "${app.notification.reminder-scan-delay-ms:600000}")
    public void sendUpcomingActivityReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusMinutes(activityReminderMinutes);
        List<VolunteerActivity> activities = volunteerActivityRepository.findByStatusAndStartTimeBetweenOrderByStartTimeAsc(
                ActivityStatus.PUBLISHED,
                now,
                deadline
        );

        for (VolunteerActivity activity : activities) {
            List<ParticipationRecord> records = participationRecordRepository.findByActivityIdAndStatusOrderByIdDesc(
                    activity.getId(),
                    ParticipationStatus.APPROVED
            );
            for (ParticipationRecord record : records) {
                UserProfile user = userProfileRepository.findById(record.getUserId()).orElse(null);
                notificationService.sendActivityReminder(user, activity);
            }
        }
    }
}
