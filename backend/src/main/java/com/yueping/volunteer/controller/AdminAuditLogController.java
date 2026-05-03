package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.AdminAuditLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AuthRequired(roles = UserRole.SUPER_ADMIN)
public class AdminAuditLogController {

    private final AdminAuditLogService adminAuditLogService;

    public AdminAuditLogController(AdminAuditLogService adminAuditLogService) {
        this.adminAuditLogService = adminAuditLogService;
    }

    @GetMapping("/audit-logs")
    public ApiResponse<?> logs() {
        return ApiResponse.ok(adminAuditLogService.listLogs());
    }
}
