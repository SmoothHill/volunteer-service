package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.ServiceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceSnapshotRepository extends JpaRepository<ServiceSnapshot, Long> {

    List<ServiceSnapshot> findByActivityIdAndUserIdOrderByCreatedAtDesc(Long activityId, Long userId);

    List<ServiceSnapshot> findByActivityIdOrderByCreatedAtDesc(Long activityId);
}

