package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.SupplementApplication;
import com.yueping.volunteer.model.SupplementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplementApplicationRepository extends JpaRepository<SupplementApplication, Long> {

    List<SupplementApplication> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<SupplementApplication> findByStatusOrderByCreatedAtDesc(SupplementStatus status);
}

