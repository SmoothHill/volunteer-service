package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.SaveServiceSiteRequest;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.ServiceSiteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/admin/service-sites")
public class ServiceSiteController {

    private final ServiceSiteService serviceSiteService;

    public ServiceSiteController(ServiceSiteService serviceSiteService) {
        this.serviceSiteService = serviceSiteService;
    }

    @GetMapping
    @AuthRequired(roles = {UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public ApiResponse<?> listSites() {
        return ApiResponse.ok(serviceSiteService.listSites());
    }

    @PostMapping
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> createSite(@Valid @RequestBody SaveServiceSiteRequest request) {
        return ApiResponse.ok("服务站点创建成功", serviceSiteService.createSite(request, AuthContext.getUserId()));
    }

    @PutMapping("/{siteId}")
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> updateSite(@PathVariable Long siteId,
                                     @Valid @RequestBody SaveServiceSiteRequest request) {
        return ApiResponse.ok("服务站点已更新", serviceSiteService.updateSite(siteId, request, AuthContext.getUserId()));
    }

    @PostMapping("/{siteId}/toggle")
    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    public ApiResponse<?> toggleSite(@PathVariable Long siteId) {
        return ApiResponse.ok("服务站点状态已切换", serviceSiteService.toggleSite(siteId, AuthContext.getUserId()));
    }
}
