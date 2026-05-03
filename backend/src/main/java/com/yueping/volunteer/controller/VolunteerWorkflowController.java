package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.CreateSnapshotRequest;
import com.yueping.volunteer.dto.SupplementApplyRequest;
import com.yueping.volunteer.dto.SupplementReviewRequest;
import com.yueping.volunteer.dto.TrackPointRequest;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.TencentMapSearchService;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/workflow")
public class VolunteerWorkflowController {

    private final VolunteerPlatformService volunteerPlatformService;
    private final TencentMapSearchService tencentMapSearchService;

    public VolunteerWorkflowController(VolunteerPlatformService volunteerPlatformService,
                                       TencentMapSearchService tencentMapSearchService) {
        this.volunteerPlatformService = volunteerPlatformService;
        this.tencentMapSearchService = tencentMapSearchService;
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/snapshots")
    public ApiResponse<?> createSnapshot(@Valid @RequestBody CreateSnapshotRequest request) {
        return ApiResponse.ok("服务快照已保存", volunteerPlatformService.createSnapshotForParticipant(AuthContext.getUserId(), request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/activities/{activityId}/snapshots")
    public ApiResponse<?> listSnapshots(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listSnapshotsForParticipant(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/track-points")
    public ApiResponse<?> trackPoint(@Valid @RequestBody TrackPointRequest request) {
        return ApiResponse.ok("爱心足迹记录成功", volunteerPlatformService.recordTrackPointForParticipant(AuthContext.getUserId(), request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/activities/{activityId}/track-points")
    public ApiResponse<?> listTrackPoints(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listTrackPointsForParticipant(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/supplements")
    public ApiResponse<?> applySupplement(@Valid @RequestBody SupplementApplyRequest request) {
        return ApiResponse.ok("补签申请已提交", volunteerPlatformService.applySupplementForParticipant(AuthContext.getUserId(), request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/supplements")
    public ApiResponse<?> mySupplements() {
        return ApiResponse.ok(volunteerPlatformService.listMySupplementsForParticipant(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/supplements/pending")
    public ApiResponse<?> pendingSupplements() {
        return ApiResponse.ok(volunteerPlatformService.listPendingSupplements(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/activities/{activityId}/records")
    public ApiResponse<?> adminActivityRecords(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listActivityRecords(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/activities/{activityId}/snapshots")
    public ApiResponse<?> adminSnapshots(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listSnapshotsForAdmin(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/activities/{activityId}/track-points")
    public ApiResponse<?> adminTrackPoints(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listTrackPointsForAdmin(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/activities/{activityId}/blockchain-proofs")
    public ApiResponse<?> adminBlockchainProofs(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listBlockchainProofsForAdmin(activityId, AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/map/search")
    public ApiResponse<?> searchLocations(@RequestParam String keyword,
                                          @RequestParam(required = false) Double latitude,
                                          @RequestParam(required = false) Double longitude) {
        return ApiResponse.ok("地点搜索成功", tencentMapSearchService.searchLocations(keyword, latitude, longitude));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/admin/supplements/{supplementId}/review")
    public ApiResponse<?> reviewSupplement(@PathVariable Long supplementId, @Valid @RequestBody SupplementReviewRequest request) {
        return ApiResponse.ok(
                "补签审核已完成",
                volunteerPlatformService.reviewSupplement(supplementId, AuthContext.getUserId(), request.getApproved(), request.getReviewComment())
        );
    }
}
