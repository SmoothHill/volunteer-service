package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.CheckInRequest;
import com.yueping.volunteer.dto.CheckOutRequest;
import com.yueping.volunteer.dto.CreateActivityRequest;
import com.yueping.volunteer.dto.EnrollRequest;
import com.yueping.volunteer.dto.EvaluateParticipationRequest;
import com.yueping.volunteer.dto.ReviewEnrollmentRequest;
import com.yueping.volunteer.model.UserRole;
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

import javax.validation.Valid;
import java.util.Collections;

@Validated
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final VolunteerPlatformService volunteerPlatformService;

    public ActivityController(VolunteerPlatformService volunteerPlatformService) {
        this.volunteerPlatformService = volunteerPlatformService;
    }

    @GetMapping
    public ApiResponse<?> listActivities() {
        return ApiResponse.ok(volunteerPlatformService.listActivities());
    }

    @GetMapping("/{activityId}")
    public ApiResponse<?> getActivity(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.getActivity(activityId));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/{activityId}/records")
    public ApiResponse<?> listActivityRecords(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listActivityRecords(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/records/{recordId}/certificate-proof")
    public ApiResponse<?> getCertificateProof(@PathVariable Long recordId) {
        return ApiResponse.ok(volunteerPlatformService.getServiceCertificate(recordId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping(value = "/records/{recordId}/certificate-proof.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportCertificateProofPdf(@PathVariable Long recordId) {
        byte[] pdf = volunteerPlatformService.getServiceCertificatePdf(recordId, AuthContext.getUserId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=service-certificate-" + recordId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/pending-enrollments")
    public ApiResponse<?> listPendingEnrollments() {
        return ApiResponse.ok(volunteerPlatformService.listPendingEnrollments(AuthContext.getUserId()));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @PostMapping
    public ApiResponse<?> createActivity(@Valid @RequestBody CreateActivityRequest request) {
        return ApiResponse.ok("活动创建成功", volunteerPlatformService.createActivity(request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/{activityId}/enroll")
    public ApiResponse<?> enroll(@PathVariable Long activityId, @RequestBody(required = false) EnrollRequest request) {
        return ApiResponse.ok("报名申请已提交，请等待管理员审核", volunteerPlatformService.enrollForParticipant(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/{activityId}/check-in")
    public ApiResponse<?> checkIn(@PathVariable Long activityId, @Valid @RequestBody CheckInRequest request) {
        return ApiResponse.ok("签到成功", volunteerPlatformService.checkInForParticipant(activityId, AuthContext.getUserId(), request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/{activityId}/check-out")
    public ApiResponse<?> checkOut(@PathVariable Long activityId, @Valid @RequestBody CheckOutRequest request) {
        return ApiResponse.ok(
                "签退成功，待管理员评价后结算积分",
                volunteerPlatformService.checkOutForParticipant(activityId, AuthContext.getUserId(), request)
        );
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @PostMapping("/{activityId}/refresh-code")
    public ApiResponse<?> refreshCode(@PathVariable Long activityId) {
        return ApiResponse.ok(
                "签到码已刷新",
                Collections.singletonMap("checkInCode", volunteerPlatformService.refreshCheckInCode(activityId))
        );
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @PostMapping("/{activityId}/records/{recordId}/confirm")
    public ApiResponse<?> confirmRecord(@PathVariable Long activityId,
                                        @PathVariable Long recordId) {
        return ApiResponse.ok(
                "服务确认已完成",
                volunteerPlatformService.confirmParticipation(recordId, AuthContext.getUserId())
        );
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @PostMapping("/{activityId}/records/{recordId}/evaluate")
    public ApiResponse<?> evaluateRecord(@PathVariable Long activityId,
                                         @PathVariable Long recordId,
                                         @Valid @RequestBody EvaluateParticipationRequest request) {
        return ApiResponse.ok(
                "服务评价已完成，积分已结算",
                volunteerPlatformService.evaluateParticipation(recordId, AuthContext.getUserId(), request.getServiceRating(), request.getServiceComment())
        );
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @PostMapping("/records/{recordId}/review-enrollment")
    public ApiResponse<?> reviewEnrollment(@PathVariable Long recordId,
                                           @RequestBody ReviewEnrollmentRequest request) {
        String message = request.isApproved() ? "报名审核已通过" : "报名审核已驳回";
        return ApiResponse.ok(message, volunteerPlatformService.reviewEnrollment(recordId, AuthContext.getUserId(), request.isApproved()));
    }
}
