package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.UpdateAnomalyStatusRequest;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/evidence")
@AuthRequired(roles = UserRole.SUPER_ADMIN)
public class EvidenceController {

    private final VolunteerPlatformService volunteerPlatformService;

    public EvidenceController(VolunteerPlatformService volunteerPlatformService) {
        this.volunteerPlatformService = volunteerPlatformService;
    }

    @GetMapping("/activities/{activityId}/records")
    public ApiResponse<?> activityRecords(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listActivityRecords(activityId, AuthContext.getUserId()));
    }

    @GetMapping("/activities/{activityId}/snapshots")
    public ApiResponse<?> snapshots(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listSnapshotsForAdmin(activityId, AuthContext.getUserId()));
    }

    @GetMapping("/activities/{activityId}/track-points")
    public ApiResponse<?> trackPoints(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listTrackPointsForAdmin(activityId, AuthContext.getUserId()));
    }

    @GetMapping("/activities/{activityId}/blockchain-proofs")
    public ApiResponse<?> blockchainProofs(@PathVariable Long activityId) {
        return ApiResponse.ok(volunteerPlatformService.listBlockchainProofsForAdmin(activityId, AuthContext.getUserId()));
    }

    @PostMapping("/activities/{activityId}/blockchain-proofs/backfill")
    public ApiResponse<?> backfillBlockchainProofs(@PathVariable Long activityId) {
        int createdCount = volunteerPlatformService.backfillBlockchainProofsForAdmin(activityId);
        return ApiResponse.ok(
                "历史区块链存证补录完成",
                createdCount
        );
    }

    @GetMapping("/anomalies")
    public ApiResponse<?> anomalies() {
        return ApiResponse.ok(volunteerPlatformService.listAnomalyRecords(AuthContext.getUserId()));
    }

    @PostMapping("/anomalies/{recordId}/status")
    public ApiResponse<?> updateAnomalyStatus(@PathVariable Long recordId,
                                              @Valid @RequestBody UpdateAnomalyStatusRequest request) {
        return ApiResponse.ok(
                "\u5f02\u5e38\u8bb0\u5f55\u5904\u7f6e\u6210\u529f",
                volunteerPlatformService.updateAnomalyStatus(
                        recordId,
                        AuthContext.getUserId(),
                        request.getStatus(),
                        request.getRemark()
                )
        );
    }
}
