package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.TrackPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackPointRepository extends JpaRepository<TrackPoint, Long> {

    List<TrackPoint> findByActivityIdAndUserIdOrderByRecordedAtAsc(Long activityId, Long userId);

    List<TrackPoint> findByActivityIdOrderByRecordedAtAsc(Long activityId);

    Optional<TrackPoint> findTopByActivityIdAndUserIdOrderByRecordedAtDesc(Long activityId, Long userId);
}
