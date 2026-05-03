package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.auth.AuthService;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.ReviewAdminApplicationRequest;
import com.yueping.volunteer.dto.SaveNotificationSubscriptionsRequest;
import com.yueping.volunteer.dto.UpdateUserRoleRequest;
import com.yueping.volunteer.dto.VerifyIdentityRequest;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.IdentityVerificationService;
import com.yueping.volunteer.service.NotificationService;
import com.yueping.volunteer.service.NotificationSubscriptionService;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api")
public class UserController {

    private final VolunteerPlatformService volunteerPlatformService;
    private final AuthService authService;
    private final IdentityVerificationService identityVerificationService;
    private final NotificationService notificationService;
    private final NotificationSubscriptionService notificationSubscriptionService;

    public UserController(VolunteerPlatformService volunteerPlatformService,
                          AuthService authService,
                          IdentityVerificationService identityVerificationService,
                          NotificationService notificationService,
                          NotificationSubscriptionService notificationSubscriptionService) {
        this.volunteerPlatformService = volunteerPlatformService;
        this.authService = authService;
        this.identityVerificationService = identityVerificationService;
        this.notificationService = notificationService;
        this.notificationSubscriptionService = notificationSubscriptionService;
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/users/dashboard")
    public ApiResponse<?> getDashboard() {
        return ApiResponse.ok(volunteerPlatformService.getDashboard(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/users/annual-report")
    public ApiResponse<?> getAnnualReport(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.getAnnualReport(AuthContext.getUserId(), year));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/users/identity")
    public ApiResponse<?> getIdentity() {
        return ApiResponse.ok(identityVerificationService.getIdentity(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/users/notifications")
    public ApiResponse<?> listMyNotifications() {
        return ApiResponse.ok(notificationService.listLatestMessages(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/users/records/{recordId}/certificate-proof")
    public ApiResponse<?> getMyCertificateProof(@PathVariable Long recordId) {
        return ApiResponse.ok(volunteerPlatformService.getMyServiceCertificate(recordId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping(value = "/users/records/{recordId}/certificate-proof.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportMyCertificateProofPdf(@PathVariable Long recordId) {
        byte[] pdf = volunteerPlatformService.getMyServiceCertificatePdf(recordId, AuthContext.getUserId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service-certificate-" + recordId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/users/notification-subscriptions")
    public ApiResponse<?> saveNotificationSubscriptions(@Valid @RequestBody SaveNotificationSubscriptionsRequest request) {
        notificationSubscriptionService.saveSubscriptions(AuthContext.getUserId(), request);
        return ApiResponse.ok("订阅授权结果已记录", true);
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/users/verify")
    public ApiResponse<?> verifyIdentity(@Valid @RequestBody VerifyIdentityRequest request) {
        return ApiResponse.ok(
                "实名认证已完成",
                identityVerificationService.verifyIdentity(AuthContext.getUserId(), request)
        );
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin/overview")
    public ApiResponse<?> getAdminOverview() {
        return ApiResponse.ok(volunteerPlatformService.getAdminOverview(AuthContext.getUserId()));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/users")
    public ApiResponse<?> listAdminUsers() {
        return ApiResponse.ok(authService.listAdminUsers());
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @PostMapping("/admin/users/{userId}/role")
    public ApiResponse<?> updateUserRole(@PathVariable Long userId, @Valid @RequestBody UpdateUserRoleRequest request) {
        return ApiResponse.ok("角色更新成功", authService.updateUserRole(userId, request.getRole()));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/admin-applications/pending")
    public ApiResponse<?> listPendingAdminApplications() {
        return ApiResponse.ok(authService.listPendingAdminApplications());
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @PostMapping("/admin/admin-applications/{applicationId}/review")
    public ApiResponse<?> reviewAdminApplication(@PathVariable Long applicationId,
                                                 @Valid @RequestBody ReviewAdminApplicationRequest request) {
        return ApiResponse.ok(
                "管理员申请已处理",
                authService.reviewAdminApplication(applicationId, AuthContext.getUserId(), request)
        );
    }
}
