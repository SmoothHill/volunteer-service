package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final VolunteerPlatformService volunteerPlatformService;

    public AnalyticsController(VolunteerPlatformService volunteerPlatformService) {
        this.volunteerPlatformService = volunteerPlatformService;
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/leaderboard")
    public ApiResponse<?> leaderboard() {
        return ApiResponse.ok(volunteerPlatformService.listLeaderboard());
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/heatmap")
    public ApiResponse<?> heatmap() {
        return ApiResponse.ok(volunteerPlatformService.buildSmartHeatmap());
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/star-leaderboard")
    public ApiResponse<?> starLeaderboard(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listStarLeaderboard(period));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/community-insights")
    public ApiResponse<?> communityInsights(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listCommunityInsights(period));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/annual-report")
    public ApiResponse<?> adminAnnualReport(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.getAdminAnnualReport(AuthContext.getUserId(), year));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/leaderboard")
    public ApiResponse<?> adminSelfLeaderboard(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.listAdminLeaderboard(year));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/community-heatmap")
    public ApiResponse<?> adminCommunityHeatmap(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.buildAdminCommunityHeatmap(AuthContext.getUserId(), year));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/activity-heatmap")
    public ApiResponse<?> adminActivityHeatmap(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.buildAdminActivityHeatmap(AuthContext.getUserId(), year));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/star-leaderboard")
    public ApiResponse<?> adminSelfStarLeaderboard(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listAdminStarLeaderboard(AuthContext.getUserId(), period));
    }

    @AuthRequired(roles = UserRole.ADMIN)
    @GetMapping("/admin-self/community-insights")
    public ApiResponse<?> adminSelfCommunityInsights(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listAdminCommunityInsights(AuthContext.getUserId(), period));
    }

    @AuthRequired(roles = {UserRole.VOLUNTEER, UserRole.ADMIN})
    @GetMapping("/recommendations")
    public ApiResponse<?> recommendations() {
        return ApiResponse.ok(volunteerPlatformService.recommendActivitiesForParticipant(AuthContext.getUserId()));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/annual-overview")
    public ApiResponse<?> annualOverview(@RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.getAnnualPlatformOverview(year));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/leaderboard")
    public ApiResponse<?> adminLeaderboard() {
        return ApiResponse.ok(volunteerPlatformService.listLeaderboardForAdmin());
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/heatmap")
    public ApiResponse<?> adminHeatmap() {
        return ApiResponse.ok(volunteerPlatformService.buildSmartHeatmapForAdmin());
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/star-leaderboard")
    public ApiResponse<?> platformStarLeaderboard(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listPlatformStarLeaderboard(period));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/community-insights")
    public ApiResponse<?> platformCommunityInsights(@RequestParam(required = false) String period) {
        return ApiResponse.ok(volunteerPlatformService.listPlatformCommunityInsights(period));
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/users")
    public ApiResponse<?> monitoredUsers() {
        return ApiResponse.ok(volunteerPlatformService.listMonitoredUsers());
    }

    @AuthRequired(roles = UserRole.SUPER_ADMIN)
    @GetMapping("/admin/users/{userId}")
    public ApiResponse<?> monitoredUserDetail(@PathVariable Long userId,
                                              @RequestParam(required = false) Integer year) {
        return ApiResponse.ok(volunteerPlatformService.getUserAnalytics(userId, year));
    }
}
