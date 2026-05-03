package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.ActivityStatus;
import com.yueping.volunteer.model.VolunteerActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VolunteerActivityRepository extends JpaRepository<VolunteerActivity, Long> {

    List<VolunteerActivity> findByStatusAndStartTimeBetweenOrderByStartTimeAsc(ActivityStatus status,
                                                                               LocalDateTime startTime,
                                                                               LocalDateTime endTime);

    List<VolunteerActivity> findByOrganizerId(Long organizerId);
}
