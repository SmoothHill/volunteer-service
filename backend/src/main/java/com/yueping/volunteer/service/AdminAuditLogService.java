package com.yueping.volunteer.service;

import com.yueping.volunteer.model.AdminAuditLog;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.repository.AdminAuditLogRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdminAuditLogService {

    private final AdminAuditLogRepository adminAuditLogRepository;
    private final UserProfileRepository userProfileRepository;

    public AdminAuditLogService(AdminAuditLogRepository adminAuditLogRepository,
                                UserProfileRepository userProfileRepository) {
        this.adminAuditLogRepository = adminAuditLogRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void log(Long operatorId, String action, String targetType, String targetId, String detail) {
        AdminAuditLog auditLog = new AdminAuditLog();
        auditLog.setOperatorId(operatorId);
        auditLog.setAction(action);
        auditLog.setTargetType(targetType);
        auditLog.setTargetId(targetId);
        auditLog.setDetail(detail);
        auditLog.setCreatedAt(LocalDateTime.now());

        if (operatorId != null) {
            userProfileRepository.findById(operatorId).ifPresent(user -> fillOperator(auditLog, user));
        }

        adminAuditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AdminAuditLog> listLogs() {
        return adminAuditLogRepository.findAllByOrderByCreatedAtDescIdDesc();
    }

    private void fillOperator(AdminAuditLog auditLog, UserProfile user) {
        auditLog.setOperatorName(user.getName());
        auditLog.setOperatorRole(user.getRole());
    }
}
