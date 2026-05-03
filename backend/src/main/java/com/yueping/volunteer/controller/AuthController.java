package com.yueping.volunteer.controller;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.auth.AuthRequired;
import com.yueping.volunteer.auth.AuthService;
import com.yueping.volunteer.common.ApiResponse;
import com.yueping.volunteer.dto.AdminApplicationRequest;
import com.yueping.volunteer.dto.SendLoginCodeRequest;
import com.yueping.volunteer.dto.SuperAdminLoginRequest;
import com.yueping.volunteer.dto.VerifyLoginCodeRequest;
import com.yueping.volunteer.dto.WechatLoginRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/wechat-login-start")
    public ApiResponse<?> wechatLoginStart(@Valid @RequestBody WechatLoginRequest request) {
        return ApiResponse.ok("微信授权成功", authService.loginWithWechatStart(request));
    }

    @PostMapping("/send-login-code")
    public ApiResponse<?> sendLoginCode(@Valid @RequestBody SendLoginCodeRequest request) {
        return ApiResponse.ok("验证码已发送", authService.sendLoginCode(request));
    }

    @PostMapping("/verify-login-code")
    public ApiResponse<?> verifyLoginCode(@Valid @RequestBody VerifyLoginCodeRequest request) {
        return ApiResponse.ok("登录成功", authService.verifyLoginCode(request));
    }

    @PostMapping("/super-admin-login")
    public ApiResponse<?> superAdminLogin(@Valid @RequestBody SuperAdminLoginRequest request) {
        return ApiResponse.ok("登录成功", authService.loginSuperAdminForWeb(request));
    }

    @PostMapping("/local-volunteer-login")
    public ApiResponse<?> localVolunteerLogin(HttpServletRequest request) {
        return ApiResponse.ok("本地志愿者登录成功", authService.loginLocalVolunteer(request == null ? null : request.getRemoteAddr()));
    }

    @AuthRequired(allowUnassigned = true)
    @GetMapping("/me")
    public ApiResponse<?> me() {
        return ApiResponse.ok(authService.currentUser());
    }

    @AuthRequired(allowUnassigned = true)
    @PostMapping("/register-volunteer")
    public ApiResponse<?> registerVolunteer() {
        return ApiResponse.ok("已注册为志愿者", authService.registerVolunteer(AuthContext.getUserId()));
    }

    @AuthRequired(allowUnassigned = true)
    @PostMapping("/admin-application")
    public ApiResponse<?> applyAdmin(@Valid @RequestBody AdminApplicationRequest request) {
        return ApiResponse.ok("管理员申请已提交", authService.applyAdminApplication(AuthContext.getUserId(), request));
    }

    @AuthRequired(allowUnassigned = true)
    @GetMapping("/my-admin-application")
    public ApiResponse<?> myAdminApplication() {
        return ApiResponse.ok(authService.getMyLatestAdminApplication(AuthContext.getUserId()));
    }
}
