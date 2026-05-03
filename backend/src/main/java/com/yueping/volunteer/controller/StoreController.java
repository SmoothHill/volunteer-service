package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.RedeemRequest;
import com.yueping.volunteer.dto.StoreItemManageRequest;
import com.yueping.volunteer.dto.UpdateRedemptionStatusRequest;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final VolunteerPlatformService volunteerPlatformService;

    public StoreController(VolunteerPlatformService volunteerPlatformService) {
        this.volunteerPlatformService = volunteerPlatformService;
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/items")
    public ApiResponse<?> items() {
        return ApiResponse.ok(volunteerPlatformService.listStoreItems());
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @PostMapping("/redeem")
    public ApiResponse<?> redeem(@Valid @RequestBody RedeemRequest request) {
        return ApiResponse.ok("兑换成功", volunteerPlatformService.redeemForParticipant(AuthContext.getUserId(), request));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/my-redemptions")
    public ApiResponse<?> myRedemptions() {
        return ApiResponse.ok(volunteerPlatformService.listMyRedemptionsForParticipant(AuthContext.getUserId()));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/items")
    public ApiResponse<?> adminItems() {
        return ApiResponse.ok(volunteerPlatformService.listStoreItemsForAdmin());
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/admin/items")
    public ApiResponse<?> createStoreItem(@Valid @RequestBody StoreItemManageRequest request) {
        return ApiResponse.ok("商品创建成功", volunteerPlatformService.createStoreItem(request));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/admin/items/{itemId}")
    public ApiResponse<?> updateStoreItem(@PathVariable Long itemId, @Valid @RequestBody StoreItemManageRequest request) {
        return ApiResponse.ok("商品更新成功", volunteerPlatformService.updateStoreItem(itemId, request));
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @GetMapping("/admin/redemptions")
    public ApiResponse<?> adminRedemptions() {
        return ApiResponse.ok(volunteerPlatformService.listAllRedemptions());
    }

    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    @PostMapping("/admin/redemptions/{redemptionId}/status")
    public ApiResponse<?> updateRedemptionStatus(@PathVariable Long redemptionId,
                                                 @Valid @RequestBody UpdateRedemptionStatusRequest request) {
        return ApiResponse.ok(
                "兑换状态更新成功",
                volunteerPlatformService.updateRedemptionStatus(redemptionId, request.getStatus(), request.getDeliveryRemark())
        );
    }
}
