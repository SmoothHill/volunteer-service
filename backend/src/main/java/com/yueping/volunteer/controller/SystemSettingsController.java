package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.UpdateSystemSettingsRequest;
import com.yueping.volunteer.init.DemoDataInitializer;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.SystemSettingService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/admin/system-settings")
public class SystemSettingsController {

    private final SystemSettingService systemSettingService;
    private final DemoDataInitializer demoDataInitializer;

    public SystemSettingsController(SystemSettingService systemSettingService,
                                    DemoDataInitializer demoDataInitializer) {
        this.systemSettingService = systemSettingService;
        this.demoDataInitializer = demoDataInitializer;
    }

    @GetMapping
    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public ApiResponse<?> getSettings() {
        return ApiResponse.ok(systemSettingService.getSettings());
    }

    @PostMapping
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> updateSettings(@Valid @RequestBody UpdateSystemSettingsRequest request) {
        return ApiResponse.ok(
                "\u7cfb\u7edf\u8bbe\u7f6e\u5df2\u4fdd\u5b58",
                systemSettingService.updateSettings(request, AuthContext.getUserId())
        );
    }

    @PostMapping("/demo-data/init")
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> initializeDemoData() {
        demoDataInitializer.ensureDemoDataEnabled();
        demoDataInitializer.initializeDemoData(AuthContext.getUserId());
        return ApiResponse.ok("\u6f14\u793a\u6570\u636e\u5df2\u8865\u9f50");
    }

    @PostMapping("/demo-data/reset")
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> resetDemoData() {
        demoDataInitializer.ensureDemoDataEnabled();
        demoDataInitializer.resetDemoData(AuthContext.getUserId());
        return ApiResponse.ok("\u6f14\u793a\u6570\u636e\u5df2\u91cd\u7f6e\u5e76\u91cd\u65b0\u521d\u59cb\u5316");
    }
}
