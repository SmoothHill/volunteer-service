package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.RedemptionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RedemptionRecordRepository extends JpaRepository<RedemptionRecord, Long> {

    List<RedemptionRecord> findByUserIdOrderByCreatedAtDesc(Long userId);
}
