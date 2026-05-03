package com.yueping.volunteer.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yueping.volunteer.dto.AdminApplicationRequest;
import com.yueping.volunteer.dto.AdminApplicationView;
import com.yueping.volunteer.dto.AdminUserView;
import com.yueping.volunteer.dto.AuthLoginResponse;
import com.yueping.volunteer.dto.ReviewAdminApplicationRequest;
import com.yueping.volunteer.dto.SendLoginCodeRequest;
import com.yueping.volunteer.dto.SendLoginCodeResponse;
import com.yueping.volunteer.dto.SuperAdminLoginRequest;
import com.yueping.volunteer.dto.VerifyLoginCodeRequest;
import com.yueping.volunteer.dto.WechatLoginRequest;
import com.yueping.volunteer.dto.WechatLoginStartResponse;
import com.yueping.volunteer.model.AdminApplication;
import com.yueping.volunteer.model.AdminApplicationStatus;
import com.yueping.volunteer.model.LoginVerificationCode;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.properties.LoginVerificationProperties;
import com.yueping.volunteer.properties.LocalTestLoginProperties;
import com.yueping.volunteer.properties.SuperAdminWebProperties;
import com.yueping.volunteer.properties.WechatProperties;
import com.yueping.volunteer.repository.AdminApplicationRepository;
import com.yueping.volunteer.repository.LoginVerificationCodeRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import com.yueping.volunteer.service.AdminAuditLogService;
import com.yueping.volunteer.service.IdentityVerificationService;
import com.yueping.volunteer.service.NotificationService;
import com.yueping.volunteer.service.SensitiveDataService;
import com.yueping.volunteer.service.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final String SYSTEM_SUPER_ADMIN_OPEN_ID = "system_super_admin_web";
    private static final String UNASSIGNED_BADGE_NAME = "待选择身份";
    private final UserProfileRepository userProfileRepository;
    private final AdminApplicationRepository adminApplicationRepository;
    private final LoginVerificationCodeRepository loginVerificationCodeRepository;
    private final JwtTokenService jwtTokenService;
    private final WechatProperties wechatProperties;
    private final LoginVerificationProperties loginVerificationProperties;
    private final LocalTestLoginProperties localTestLoginProperties;
    private final SuperAdminWebProperties superAdminWebProperties;
    private final AdminAuditLogService adminAuditLogService;
    private final IdentityVerificationService identityVerificationService;
    private final NotificationService notificationService;
    private final SmsSender smsSender;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AuthService(UserProfileRepository userProfileRepository,
                       AdminApplicationRepository adminApplicationRepository,
                       LoginVerificationCodeRepository loginVerificationCodeRepository,
                       JwtTokenService jwtTokenService,
                       WechatProperties wechatProperties,
                       LoginVerificationProperties loginVerificationProperties,
                       LocalTestLoginProperties localTestLoginProperties,
                       SuperAdminWebProperties superAdminWebProperties,
                       AdminAuditLogService adminAuditLogService,
                       IdentityVerificationService identityVerificationService,
                       NotificationService notificationService,
                       SmsSender smsSender) {
        this.userProfileRepository = userProfileRepository;
        this.adminApplicationRepository = adminApplicationRepository;
        this.loginVerificationCodeRepository = loginVerificationCodeRepository;
        this.jwtTokenService = jwtTokenService;
        this.wechatProperties = wechatProperties;
        this.loginVerificationProperties = loginVerificationProperties;
        this.localTestLoginProperties = localTestLoginProperties;
        this.superAdminWebProperties = superAdminWebProperties;
        this.adminAuditLogService = adminAuditLogService;
        this.identityVerificationService = identityVerificationService;
        this.notificationService = notificationService;
        this.smsSender = smsSender;
    }

    public WechatLoginStartResponse loginWithWechatStart(WechatLoginRequest request) {
        String url = UriComponentsBuilder.fromHttpUrl(wechatProperties.getCode2sessionUrl())
                .queryParam("appid", wechatProperties.getAppId())
                .queryParam("secret", wechatProperties.getAppSecret())
                .queryParam("js_code", request.getCode())
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        Map<String, Object> result = requestWechatSession(url);
        if (result == null || !result.containsKey("openid")) {
            String errorMessage = result == null
                    ? "微信登录失败"
                    : String.valueOf(result.getOrDefault("errmsg", "微信登录失败"));
            throw new IllegalArgumentException(errorMessage);
        }

        String openId = String.valueOf(result.get("openid"));
        UserProfile userProfile = userProfileRepository.findByOpenId(openId)
                .orElseGet(() -> createWechatUser(openId, request));

        if (StringUtils.hasText(request.getNickname()) && !request.getNickname().equals(userProfile.getName())) {
            userProfile.setName(request.getNickname());
        }
        if (StringUtils.hasText(request.getAvatarUrl())) {
            userProfile.setAvatarUrl(request.getAvatarUrl());
        }

        userProfile = userProfileRepository.save(userProfile);
        if (!shouldRequirePhoneVerification(userProfile)) {
            return WechatLoginStartResponse.directLogin(new AuthLoginResponse(jwtTokenService.createToken(userProfile), userProfile));
        }

        String loginTicket = UUID.randomUUID().toString().replace("-", "");
        storeLoginTicket(loginTicket, userProfile);
        return WechatLoginStartResponse.pendingPhoneVerification(loginTicket);
    }

    public SendLoginCodeResponse sendLoginCode(SendLoginCodeRequest request) {
        UserProfile userProfile = resolveUserByLoginTicket(request.getLoginTicket());
        String phoneNo = normalizePhoneNo(request.getPhoneNo());
        String phoneHash = SensitiveDataService.hashForLookup(phoneNo);
        if (userProfileRepository.existsByPhoneHashAndIdNot(phoneHash, userProfile.getId())) {
            throw new IllegalArgumentException("该手机号已被其他账号绑定");
        }

        Optional<LoginVerificationCode> latestOptional = loginVerificationCodeRepository
                .findTopByLoginTicketAndPhoneHashOrderByCreatedAtDesc(request.getLoginTicket(), phoneHash);
        if (latestOptional.isPresent()) {
            LocalDateTime latestCreatedAt = latestOptional.get().getCreatedAt();
            if (latestCreatedAt != null && latestCreatedAt.plusSeconds(loginVerificationProperties.getResendCooldownSeconds()).isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("验证码发送过于频繁，请稍后再试");
            }
        }

        String code = buildVerificationCode();
        LoginVerificationCode verificationCode = new LoginVerificationCode();
        verificationCode.setLoginTicket(request.getLoginTicket());
        verificationCode.setPhoneNo(phoneNo);
        verificationCode.setPhoneHash(phoneHash);
        verificationCode.setCode(code);
        verificationCode.setUsed(false);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(loginVerificationProperties.getCodeExpireMinutes()));
        loginVerificationCodeRepository.save(verificationCode);

        smsSender.sendLoginVerificationCode(phoneNo, code);
        return new SendLoginCodeResponse(smsSender.isMock(), smsSender.isMock() ? code : null);
    }

    public AuthLoginResponse verifyLoginCode(VerifyLoginCodeRequest request) {
        UserProfile userProfile = resolveUserByLoginTicket(request.getLoginTicket());
        String phoneNo = normalizePhoneNo(request.getPhoneNo());
        String phoneHash = SensitiveDataService.hashForLookup(phoneNo);
        LoginVerificationCode verificationCode = loginVerificationCodeRepository
                .findTopByLoginTicketAndPhoneHashOrderByCreatedAtDesc(request.getLoginTicket(), phoneHash)
                .orElseThrow(() -> new IllegalArgumentException("请先获取验证码"));

        if (Boolean.TRUE.equals(verificationCode.getUsed())) {
            throw new IllegalArgumentException("验证码已使用，请重新获取");
        }
        if (verificationCode.getExpiredAt() == null || verificationCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("验证码已过期，请重新获取");
        }
        if (!String.valueOf(verificationCode.getCode()).equals(String.valueOf(request.getCode()))) {
            throw new IllegalArgumentException("验证码错误");
        }
        if (userProfileRepository.existsByPhoneHashAndIdNot(phoneHash, userProfile.getId())) {
            throw new IllegalArgumentException("该手机号已被其他账号绑定");
        }

        verificationCode.setUsed(true);
        loginVerificationCodeRepository.save(verificationCode);

        userProfile.setPhoneNo(phoneNo);
        userProfile.setPhoneHash(phoneHash);
        userProfile.setPhoneVerifiedAt(LocalDateTime.now());
        userProfile = userProfileRepository.save(userProfile);
        return new AuthLoginResponse(jwtTokenService.createToken(userProfile), userProfile);
    }

    public AuthLoginResponse loginSuperAdminForWeb(SuperAdminLoginRequest request) {
        verifyWebLoginCredentials(request);
        UserProfile superAdmin = resolveOrCreateSystemSuperAdminForWebLogin();
        return new AuthLoginResponse(jwtTokenService.createToken(superAdmin), superAdmin);
    }

    public AuthLoginResponse loginLocalVolunteer() {
        if (!localTestLoginProperties.isEnabled()) {
            throw new IllegalArgumentException("本地测试登录未开启");
        }

        UserProfile volunteer = userProfileRepository.findByOpenId(localTestLoginProperties.getVolunteerOpenId())
                .orElseGet(this::createLocalVolunteerUser);

        volunteer.setRole(UserRole.VOLUNTEER);
        volunteer.setBadgeName(resolveRoleBadge(UserRole.VOLUNTEER, volunteer.getTotalHours()));
        if (!StringUtils.hasText(volunteer.getName())) {
            volunteer.setName(localTestLoginProperties.getVolunteerName());
        }

        volunteer = userProfileRepository.save(volunteer);
        return new AuthLoginResponse(jwtTokenService.createToken(volunteer), volunteer);
    }

    public AuthLoginResponse loginLocalVolunteer(String remoteAddr) {
        if (localTestLoginProperties.isEnabled()) {
            UserProfile volunteer = userProfileRepository.findByOpenId(localTestLoginProperties.getVolunteerOpenId())
                    .orElseGet(this::createLocalVolunteerUser);

            volunteer.setRole(UserRole.VOLUNTEER);
            volunteer.setBadgeName(resolveRoleBadge(UserRole.VOLUNTEER, volunteer.getTotalHours()));
            if (!StringUtils.hasText(volunteer.getName())) {
                volunteer.setName(localTestLoginProperties.getVolunteerName());
            }

            volunteer = userProfileRepository.save(volunteer);
            return new AuthLoginResponse(jwtTokenService.createToken(volunteer), volunteer);
        }
        throw new IllegalArgumentException("本地测试登录未开启，请先在本机测试或设置 APP_LOCAL_TEST_LOGIN_ENABLED=true");
    }

    private boolean shouldRequirePhoneVerification(UserProfile userProfile) {
        if (!loginVerificationProperties.isEnabled()) {
            return false;
        }
        return !StringUtils.hasText(userProfile.getPhoneNo());
    }

    private void storeLoginTicket(String loginTicket, UserProfile userProfile) {
        LoginVerificationCode verificationCode = new LoginVerificationCode();
        verificationCode.setLoginTicket(loginTicket);
        verificationCode.setPhoneNo("");
        verificationCode.setPhoneHash(String.valueOf(userProfile.getId()));
        verificationCode.setCode("");
        verificationCode.setUsed(false);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiredAt(LocalDateTime.now().plusMinutes(loginVerificationProperties.getCodeExpireMinutes()));
        loginVerificationCodeRepository.save(verificationCode);
    }

    private UserProfile resolveUserByLoginTicket(String loginTicket) {
        LoginVerificationCode verificationCode = loginVerificationCodeRepository
                .findTopByLoginTicketAndCodeOrderByCreatedAtDesc(loginTicket, "")
                .orElseThrow(() -> new IllegalArgumentException("登录票据无效，请重新微信授权"));
        if (verificationCode.getExpiredAt() == null || verificationCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("登录票据已过期，请重新微信授权");
        }
        Long userId = parseLoginTicketUserId(verificationCode.getPhoneHash());
        return findUser(userId, "登录用户不存在");
    }

    private Long parseLoginTicketUserId(String value) {
        try {
            return Long.valueOf(value);
        } catch (Exception ex) {
            throw new IllegalArgumentException("登录票据无效，请重新微信授权");
        }
    }

    private String normalizePhoneNo(String phoneNo) {
        String normalized = phoneNo == null ? "" : phoneNo.trim();
        if (!normalized.matches("^1\\d{10}$")) {
            throw new IllegalArgumentException("手机号格式不正确");
        }
        return normalized;
    }

    private String buildVerificationCode() {
        int value = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(value);
    }

    @Transactional(readOnly = true)
    public UserProfile currentUser() {
        Long userId = AuthContext.getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("当前未登录");
        }
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("当前用户不存在"));
    }

    @Transactional(readOnly = true)
    public List<AdminUserView> listAdminUsers() {
        return userProfileRepository.findAll().stream()
                .filter(this::isManagedWechatUser)
                .sorted(Comparator.comparing(UserProfile::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(UserProfile::getId, Comparator.reverseOrder()))
                .map(AdminUserView::from)
                .collect(Collectors.toList());
    }

    public AdminUserView updateUserRole(Long userId, UserRole role) {
        if (role == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("超级管理员只能由系统预置，不能在这里直接分配");
        }

        UserProfile operator = findUser(AuthContext.getUserId(), "操作用户不存在");
        identityVerificationService.ensureVerified(operator, "管理用户角色");
        UserProfile targetUser = findUser(userId, "用户不存在");
        if (!isManagedWechatUser(targetUser)) {
            throw new IllegalArgumentException("当前用户不是可管理的微信账号");
        }
        if (targetUser.getRole() == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("系统超级管理员不允许在这里修改");
        }

        UserRole oldRole = targetUser.getRole();
        targetUser.setRole(role);
        targetUser.setBadgeName(resolveRoleBadge(role, targetUser.getTotalHours()));
        userProfileRepository.save(targetUser);

        adminAuditLogService.log(
                AuthContext.getUserId(),
                "USER_ROLE_UPDATED",
                "USER",
                String.valueOf(targetUser.getId()),
                "将用户 " + resolveUserName(targetUser) + " 的角色从 " + oldRole + " 调整为 " + role
        );
        return AdminUserView.from(targetUser);
    }

    public UserProfile registerVolunteer(Long userId) {
        UserProfile user = findUser(userId, "用户不存在");
        ensureWechatUser(user);
        if (user.getRole() == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("系统超级管理员无需注册为志愿者");
        }
        if (user.getRole() == UserRole.ADMIN) {
            return user;
        }
        user.setRole(UserRole.VOLUNTEER);
        user.setBadgeName(resolveRoleBadge(UserRole.VOLUNTEER, user.getTotalHours()));
        return userProfileRepository.save(user);
    }

    public AdminApplicationView applyAdminApplication(Long userId, AdminApplicationRequest request) {
        UserProfile user = findUser(userId, "用户不存在");
        ensureWechatUser(user);
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("当前账号已经是活动管理员");
        }
        if (user.getRole() == UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("超级管理员无需提交活动管理员申请");
        }
        identityVerificationService.ensureVerified(user, "申请活动管理员");
        if (adminApplicationRepository.existsByUserIdAndStatus(userId, AdminApplicationStatus.PENDING)) {
            throw new IllegalArgumentException("你已有待审核的管理员申请，请耐心等待");
        }

        if (user.getRole() == null) {
            user.setRole(UserRole.VOLUNTEER);
            user.setBadgeName(resolveRoleBadge(UserRole.VOLUNTEER, user.getTotalHours()));
            userProfileRepository.save(user);
        }

        AdminApplication application = new AdminApplication();
        application.setUserId(userId);
        application.setReason(request.getReason().trim());
        application.setStatus(AdminApplicationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());
        adminApplicationRepository.save(application);
        notificationService.sendAdminApplicationSubmitted(user, application.getId());

        adminAuditLogService.log(
                userId,
                "ADMIN_APPLICATION_CREATED",
                "ADMIN_APPLICATION",
                String.valueOf(application.getId()),
                "提交活动管理员申请"
        );
        return AdminApplicationView.from(application, user, null);
    }

    @Transactional(readOnly = true)
    public AdminApplicationView getMyLatestAdminApplication(Long userId) {
        Optional<AdminApplication> latest = adminApplicationRepository.findTopByUserIdOrderByCreatedAtDescIdDesc(userId);
        if (!latest.isPresent()) {
            return null;
        }
        UserProfile user = userProfileRepository.findById(userId).orElse(null);
        UserProfile reviewer = latest.get().getReviewerId() == null
                ? null
                : userProfileRepository.findById(latest.get().getReviewerId()).orElse(null);
        return AdminApplicationView.from(latest.get(), user, reviewer);
    }

    @Transactional(readOnly = true)
    public List<AdminApplicationView> listPendingAdminApplications() {
        return adminApplicationRepository.findAllByStatusOrderByCreatedAtDescIdDesc(AdminApplicationStatus.PENDING).stream()
                .map(application -> AdminApplicationView.from(
                        application,
                        userProfileRepository.findById(application.getUserId()).orElse(null),
                        application.getReviewerId() == null ? null : userProfileRepository.findById(application.getReviewerId()).orElse(null)
                ))
                .collect(Collectors.toList());
    }

    public AdminApplicationView reviewAdminApplication(Long applicationId, Long reviewerId, ReviewAdminApplicationRequest request) {
        AdminApplication application = adminApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("管理员申请不存在"));
        if (application.getStatus() != AdminApplicationStatus.PENDING) {
            throw new IllegalArgumentException("该申请已经处理过了");
        }

        UserProfile applicant = findUser(application.getUserId(), "申请用户不存在");
        UserProfile reviewer = findUser(reviewerId, "审核人不存在");
        identityVerificationService.ensureVerified(reviewer, "审核活动管理员申请");

        boolean approved = Boolean.TRUE.equals(request.getApproved());
        application.setStatus(approved ? AdminApplicationStatus.APPROVED : AdminApplicationStatus.REJECTED);
        application.setReviewComment(resolveReviewComment(request, approved));
        application.setReviewerId(reviewerId);
        application.setReviewedAt(LocalDateTime.now());
        adminApplicationRepository.save(application);

        if (approved) {
            applicant.setRole(UserRole.ADMIN);
            applicant.setBadgeName(resolveRoleBadge(UserRole.ADMIN, applicant.getTotalHours()));
            userProfileRepository.save(applicant);
        }
        notificationService.sendAdminApplicationReviewed(applicant, application.getId(), application.getStatus());
        notificationService.sendAdminApplicationReviewedOperator(reviewer, applicant, application.getId(), application.getStatus());

        adminAuditLogService.log(
                reviewerId,
                approved ? "ADMIN_APPLICATION_APPROVED" : "ADMIN_APPLICATION_REJECTED",
                "ADMIN_APPLICATION",
                String.valueOf(applicationId),
                (approved ? "通过" : "驳回") + "了用户 " + resolveUserName(applicant) + " 的活动管理员申请"
        );

        return AdminApplicationView.from(application, applicant, reviewer);
    }

    private UserProfile createWechatUser(String openId, WechatLoginRequest request) {
        UserProfile userProfile = new UserProfile();
        userProfile.setOpenId(openId);
        userProfile.setName(StringUtils.hasText(request.getNickname())
                ? request.getNickname()
                : "微信用户" + UUID.randomUUID().toString().substring(0, 6));
        userProfile.setAvatarUrl(request.getAvatarUrl());
        userProfile.setRealName("");
        userProfile.setIdCardNo("");
        userProfile.setIdCardHash("");
        userProfile.setVolunteerCardNo("");
        userProfile.setVerified(false);
        userProfile.setRole(null);
        userProfile.setTotalHours(0);
        userProfile.setTotalPoints(0);
        userProfile.setCreditScore(100);
        userProfile.setBadgeName(UNASSIGNED_BADGE_NAME);
        userProfile.setCreatedAt(LocalDateTime.now());
        return userProfileRepository.save(userProfile);
    }

    private UserProfile createLocalVolunteerUser() {
        UserProfile userProfile = new UserProfile();
        userProfile.setOpenId(localTestLoginProperties.getVolunteerOpenId());
        userProfile.setName(localTestLoginProperties.getVolunteerName());
        userProfile.setAvatarUrl("");
        userProfile.setRealName("本地测试志愿者");
        userProfile.setIdCardNo("");
        userProfile.setIdCardHash("");
        userProfile.setVolunteerCardNo("");
        userProfile.setVerified(false);
        userProfile.setRole(UserRole.VOLUNTEER);
        userProfile.setTotalHours(0);
        userProfile.setTotalPoints(0);
        userProfile.setCreditScore(100);
        userProfile.setBadgeName(resolveRoleBadge(UserRole.VOLUNTEER, 0));
        userProfile.setCreatedAt(LocalDateTime.now());
        return userProfileRepository.save(userProfile);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> requestWechatSession(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestProperty("Accept", "application/json");
            if (connection.getResponseCode() >= 400) {
                return objectMapper.readValue(connection.getErrorStream(), Map.class);
            }
            return objectMapper.readValue(connection.getInputStream(), Map.class);
        } catch (IOException ex) {
            throw new IllegalArgumentException("连接微信登录服务失败，请检查本机代理或网络设置");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void verifyWebLoginCredentials(SuperAdminLoginRequest request) {
        String username = resolveSuperAdminUsername();
        String password = resolveSuperAdminPassword();
        if (!username.equals(request.getUsername())
                || !password.equals(request.getPassword())) {
            throw new IllegalArgumentException("账号或密码错误");
        }
    }

    private String resolveSuperAdminUsername() {
        if (StringUtils.hasText(superAdminWebProperties.getWebUsername())) {
            return superAdminWebProperties.getWebUsername().trim();
        }
        throw new IllegalStateException("Missing APP_SUPER_ADMIN_USERNAME");
    }

    private String resolveSuperAdminPassword() {
        if (StringUtils.hasText(superAdminWebProperties.getWebPassword())) {
            return superAdminWebProperties.getWebPassword().trim();
        }
        throw new IllegalStateException("Missing APP_SUPER_ADMIN_PASSWORD");
    }

    private UserProfile resolveOrCreateSystemSuperAdminForWebLogin() {
        return userProfileRepository.findByOpenId(SYSTEM_SUPER_ADMIN_OPEN_ID)
                .map(existing -> {
                    if (existing.getRole() != UserRole.SUPER_ADMIN) {
                        existing.setRole(UserRole.SUPER_ADMIN);
                        existing.setBadgeName(resolveRoleBadge(UserRole.SUPER_ADMIN, existing.getTotalHours()));
                    }
                    if (!StringUtils.hasText(existing.getRealName())) {
                        existing.setRealName("");
                    }
                    if (!StringUtils.hasText(existing.getIdCardNo())) {
                        existing.setIdCardNo("");
                    }
                    if (!StringUtils.hasText(existing.getIdCardHash())) {
                        existing.setIdCardHash("");
                    }
                    if (existing.getVolunteerCardNo() == null) {
                        existing.setVolunteerCardNo("");
                    }
                    existing.setVerified(Boolean.TRUE.equals(existing.getVerified())
                            && StringUtils.hasText(existing.getRealName())
                            && StringUtils.hasText(existing.getIdCardNo()));
                    userProfileRepository.save(existing);
                    return existing;
                })
                .orElseGet(() -> {
                    UserProfile user = new UserProfile();
                    user.setOpenId(SYSTEM_SUPER_ADMIN_OPEN_ID);
                    user.setName("系统超级管理员");
                    user.setRole(UserRole.SUPER_ADMIN);
                    user.setAvatarUrl("");
                    user.setRealName("");
                    user.setIdCardNo("");
                    user.setIdCardHash("");
                    user.setVolunteerCardNo("");
                    user.setVerified(false);
                    user.setTotalHours(0);
                    user.setTotalPoints(0);
                    user.setCreditScore(100);
                    user.setBadgeName(resolveRoleBadge(UserRole.SUPER_ADMIN, 0));
                    user.setCreatedAt(LocalDateTime.now());
                    return userProfileRepository.save(user);
                });
    }

    private UserProfile findUser(Long userId, String message) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(message));
    }

    private void ensureWechatUser(UserProfile user) {
        if (!isManagedWechatUser(user)) {
            throw new IllegalArgumentException("当前账号不是可操作的微信用户");
        }
    }

    private boolean isManagedWechatUser(UserProfile userProfile) {
        return StringUtils.hasText(userProfile.getOpenId())
                && !userProfile.getOpenId().startsWith("dev_")
                && !userProfile.getOpenId().startsWith("demo_")
                && !userProfile.getOpenId().startsWith("system_")
                && !userProfile.getOpenId().startsWith("local_test_");
    }

    private String resolveReviewComment(ReviewAdminApplicationRequest request, boolean approved) {
        if (StringUtils.hasText(request.getReviewComment())) {
            return request.getReviewComment().trim();
        }
        return approved ? "审核通过，已升级为活动管理员" : "审核未通过";
    }

    private String resolveUserName(UserProfile user) {
        return StringUtils.hasText(user.getName()) ? user.getName() : "用户" + user.getId();
    }

    private String resolveRoleBadge(UserRole role, double totalHours) {
        if (role == null) {
            return UNASSIGNED_BADGE_NAME;
        }
        if (role == UserRole.SUPER_ADMIN) {
            return "系统超级管理员";
        }
        if (role == UserRole.ADMIN) {
            return "活动管理员";
        }
        return resolveVolunteerBadge(totalHours);
    }

    private String resolveVolunteerBadge(double totalHours) {
        if (totalHours >= 100) {
            return "五星志愿者";
        }
        if (totalHours >= 60) {
            return "四星志愿者";
        }
        if (totalHours >= 30) {
            return "三星志愿者";
        }
        if (totalHours >= 10) {
            return "二星志愿者";
        }
        return "一星志愿者";
    }
}
