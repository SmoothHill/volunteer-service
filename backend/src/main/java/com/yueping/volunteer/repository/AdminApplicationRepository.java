package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.AdminApplication;
import com.yueping.volunteer.model.AdminApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminApplicationRepository extends JpaRepository<AdminApplication, Long> {

    boolean existsByUserIdAndStatus(Long userId, AdminApplicationStatus status);

    Optional<AdminApplication> findTopByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    List<AdminApplication> findAllByStatusOrderByCreatedAtDescIdDesc(AdminApplicationStatus status);
}
