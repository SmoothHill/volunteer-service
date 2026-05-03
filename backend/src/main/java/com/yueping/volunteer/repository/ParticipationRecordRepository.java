package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.ParticipationRecord;
import com.yueping.volunteer.model.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRecordRepository extends JpaRepository<ParticipationRecord, Long> {

    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    Optional<ParticipationRecord> findByActivityIdAndUserId(Long activityId, Long userId);

    List<ParticipationRecord> findByActivityIdOrderByIdDesc(Long activityId);

    List<ParticipationRecord> findByActivityIdAndStatusOrderByIdDesc(Long activityId, ParticipationStatus status);

    List<ParticipationRecord> findByUserIdOrderByIdDesc(Long userId);

    List<ParticipationRecord> findByStatusOrderByIdDesc(ParticipationStatus status);

    long countByStatus(ParticipationStatus status);
}
