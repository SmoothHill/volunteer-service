package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.BlockchainProofRecord;
import com.yueping.volunteer.model.BlockchainEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BlockchainProofRecordRepository extends JpaRepository<BlockchainProofRecord, Long> {

    List<BlockchainProofRecord> findByActivityIdOrderByEventTimeAscCreatedAtAsc(Long activityId);

    List<BlockchainProofRecord> findByRecordIdOrderByCreatedAtDesc(Long recordId);

    boolean existsByRecordIdAndEventType(Long recordId, BlockchainEventType eventType);

    boolean existsByRecordIdAndEventTypeAndEventTime(Long recordId,
                                                     BlockchainEventType eventType,
                                                     LocalDateTime eventTime);
}
