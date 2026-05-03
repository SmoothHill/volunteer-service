package com.yueping.volunteer.service;

import com.yueping.volunteer.auth.AuthContext;
import com.yueping.volunteer.cache.CheckInCodeCacheService;
import com.yueping.volunteer.certificate.CertificatePayload;
import com.yueping.volunteer.certificate.CertificateService;
import com.yueping.volunteer.certificate.ServiceCertificatePdfService;
import com.yueping.volunteer.dto.CheckInRequest;
import com.yueping.volunteer.dto.CheckOutRequest;
import com.yueping.volunteer.dto.CreateActivityRequest;
import com.yueping.volunteer.dto.CreateSnapshotRequest;
import com.yueping.volunteer.dto.RedeemRequest;
import com.yueping.volunteer.dto.StoreItemManageRequest;
import com.yueping.volunteer.dto.SupplementApplyRequest;
import com.yueping.volunteer.dto.TrackPointRequest;
import com.yueping.volunteer.model.ActivityRecordView;
import com.yueping.volunteer.model.ActivityStatus;
import com.yueping.volunteer.model.AdminAnnualReportView;
import com.yueping.volunteer.model.AdminLeaderboardView;
import com.yueping.volunteer.model.AdminMonitoredUserView;
import com.yueping.volunteer.model.AnnualFootprintPointView;
import com.yueping.volunteer.model.AnnualPlatformOverviewView;
import com.yueping.volunteer.model.AnomalyStatus;
import com.yueping.volunteer.model.AreaStatView;
import com.yueping.volunteer.model.AdminRedemptionView;
import com.yueping.volunteer.model.AdminUserAnalyticsView;
import com.yueping.volunteer.model.AdminOverview;
import com.yueping.volunteer.model.BlockchainEventType;
import com.yueping.volunteer.model.BlockchainProofRecord;
import com.yueping.volunteer.model.BlockchainProofView;
import com.yueping.volunteer.model.CheckInMethod;
import com.yueping.volunteer.model.HeatmapPointView;
import com.yueping.volunteer.model.LeaderboardView;
import com.yueping.volunteer.model.CommunityInsightView;
import com.yueping.volunteer.model.ParticipationRecord;
import com.yueping.volunteer.model.ParticipationStatus;
import com.yueping.volunteer.model.RedemptionRecord;
import com.yueping.volunteer.model.RedemptionStatus;
import com.yueping.volunteer.model.RecommendedActivityView;
import com.yueping.volunteer.model.ServiceCertificateView;
import com.yueping.volunteer.model.ServiceSite;
import com.yueping.volunteer.model.ServiceSnapshot;
import com.yueping.volunteer.model.StoreItem;
import com.yueping.volunteer.model.StoreItemDeliveryType;
import com.yueping.volunteer.model.StarLeaderboardView;
import com.yueping.volunteer.model.SupplementApplication;
import com.yueping.volunteer.model.SupplementStatus;
import com.yueping.volunteer.model.SupplementType;
import com.yueping.volunteer.model.TrackPoint;
import com.yueping.volunteer.model.TrackPointType;
import com.yueping.volunteer.model.TrendStatView;
import com.yueping.volunteer.model.UserDashboardView;
import com.yueping.volunteer.model.UserEvidenceSummaryView;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.model.VolunteerAnnualReportView;
import com.yueping.volunteer.model.VolunteerActivity;
import com.yueping.volunteer.repository.BlockchainProofRecordRepository;
import com.yueping.volunteer.repository.ParticipationRecordRepository;
import com.yueping.volunteer.repository.RedemptionRecordRepository;
import com.yueping.volunteer.repository.ServiceSiteRepository;
import com.yueping.volunteer.repository.ServiceSnapshotRepository;
import com.yueping.volunteer.repository.StoreItemRepository;
import com.yueping.volunteer.repository.SupplementApplicationRepository;
import com.yueping.volunteer.repository.TrackPointRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import com.yueping.volunteer.repository.VolunteerActivityRepository;
import com.yueping.volunteer.file.FileStorageService;
import com.yueping.volunteer.util.SensitiveMaskingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Transactional
public class VolunteerPlatformService {

    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final int FREQUENT_SUPPLEMENT_DAYS = 30;
    private static final String PERIOD_WEEK = "week";
    private static final String PERIOD_MONTH = "month";
    private static final int RECOMMENDATION_LIMIT = 5;

    private final VolunteerActivityRepository volunteerActivityRepository;
    private final UserProfileRepository userProfileRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final CheckInCodeCacheService checkInCodeCacheService;
    private final ServiceSnapshotRepository serviceSnapshotRepository;
    private final ServiceSiteRepository serviceSiteRepository;
    private final SupplementApplicationRepository supplementApplicationRepository;
    private final TrackPointRepository trackPointRepository;
    private final BlockchainProofRecordRepository blockchainProofRecordRepository;
    private final StoreItemRepository storeItemRepository;
    private final RedemptionRecordRepository redemptionRecordRepository;
    private final CertificateService certificateService;
    private final ServiceCertificatePdfService serviceCertificatePdfService;
    private final AdminAuditLogService adminAuditLogService;
    private final SystemSettingService systemSettingService;
    private final RewardEngineService rewardEngineService;
    private final IdentityVerificationService identityVerificationService;
    private final BlockchainProofService blockchainProofService;
    private final NotificationService notificationService;
    private final FileStorageService fileStorageService;
    private final Random random = new Random();

    public VolunteerPlatformService(VolunteerActivityRepository volunteerActivityRepository,
                                    UserProfileRepository userProfileRepository,
                                    ParticipationRecordRepository participationRecordRepository,
                                    CheckInCodeCacheService checkInCodeCacheService,
                                    ServiceSnapshotRepository serviceSnapshotRepository,
                                    ServiceSiteRepository serviceSiteRepository,
                                    SupplementApplicationRepository supplementApplicationRepository,
                                    TrackPointRepository trackPointRepository,
                                    BlockchainProofRecordRepository blockchainProofRecordRepository,
                                    StoreItemRepository storeItemRepository,
                                    RedemptionRecordRepository redemptionRecordRepository,
                                    CertificateService certificateService,
                                    ServiceCertificatePdfService serviceCertificatePdfService,
                                    AdminAuditLogService adminAuditLogService,
                                    SystemSettingService systemSettingService,
                                    RewardEngineService rewardEngineService,
                                    IdentityVerificationService identityVerificationService,
                                    BlockchainProofService blockchainProofService,
                                    NotificationService notificationService,
                                    FileStorageService fileStorageService) {
        this.volunteerActivityRepository = volunteerActivityRepository;
        this.userProfileRepository = userProfileRepository;
        this.participationRecordRepository = participationRecordRepository;
        this.checkInCodeCacheService = checkInCodeCacheService;
        this.serviceSnapshotRepository = serviceSnapshotRepository;
        this.serviceSiteRepository = serviceSiteRepository;
        this.supplementApplicationRepository = supplementApplicationRepository;
        this.trackPointRepository = trackPointRepository;
        this.blockchainProofRecordRepository = blockchainProofRecordRepository;
        this.storeItemRepository = storeItemRepository;
        this.redemptionRecordRepository = redemptionRecordRepository;
        this.certificateService = certificateService;
        this.serviceCertificatePdfService = serviceCertificatePdfService;
        this.adminAuditLogService = adminAuditLogService;
        this.systemSettingService = systemSettingService;
        this.rewardEngineService = rewardEngineService;
        this.identityVerificationService = identityVerificationService;
        this.blockchainProofService = blockchainProofService;
        this.notificationService = notificationService;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<VolunteerActivity> listActivities() {
        return volunteerActivityRepository.findAll().stream()
                .sorted(Comparator.comparing(VolunteerActivity::getStartTime))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VolunteerActivity getActivity(Long activityId) {
        return volunteerActivityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("\u6d3b\u52a8\u4e0d\u5b58\u5728"));
    }

    public VolunteerActivity createActivity(CreateActivityRequest request) {
        return createActivity(request, AuthContext.getUserId());
    }

    public VolunteerActivity createActivity(CreateActivityRequest request, Long operatorId) {
        UserProfile operator = ensureVerifiedOperator(operatorId, "\u521b\u5efa\u6d3b\u52a8");
        ServiceSite serviceSite = null;
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("\u7ed3\u675f\u65f6\u95f4\u4e0d\u80fd\u65e9\u4e8e\u5f00\u59cb\u65f6\u95f4");
        }

        if (request.getServiceSiteId() != null) {
            serviceSite = serviceSiteRepository.findById(request.getServiceSiteId())
                    .orElseThrow(() -> new IllegalArgumentException("\u670d\u52a1\u7ad9\u70b9\u4e0d\u5b58\u5728"));
        }

        VolunteerActivity activity = new VolunteerActivity();
        activity.setTitle(request.getTitle());
        activity.setDescription(request.getDescription());
        activity.setLocation(request.getLocation());
        activity.setCategory(normalizeActivityCategory(request.getCategory()));
        activity.setDifficultyLevel(resolveDifficultyLevel(request.getDifficultyLevel()));
        activity.setDifficultyCoefficient(rewardEngineService.resolveDifficultyCoefficient(
                activity.getDifficultyLevel(),
                request.getDifficultyCoefficient()
        ));
        activity.setDemandLevel(resolveDemandLevel(request.getDemandLevel()));
        activity.setServiceSiteId(request.getServiceSiteId());
        activity.setLatitude(request.getLatitude());
        activity.setLongitude(request.getLongitude());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setCapacity(request.getCapacity());
        activity.setEnrolledCount(0);
        activity.setOrganizerId(operator.getId());
        int geofenceRadius = request.getGeofenceRadiusMeters() > 0
                ? request.getGeofenceRadiusMeters()
                : serviceSite == null
                ? systemSettingService.getDefaultGeofenceRadiusMeters()
                : serviceSite.getRecommendedRadiusMeters();
        activity.setGeofenceRadiusMeters(geofenceRadius);
        activity.setStatus(ActivityStatus.PUBLISHED);
        activity.setCheckInCode(generateCode());

        VolunteerActivity saved = volunteerActivityRepository.save(activity);
        checkInCodeCacheService.cacheCode(saved.getId(), saved.getCheckInCode());
        adminAuditLogService.log(
                operatorId,
                "ACTIVITY_CREATED",
                "ACTIVITY",
                String.valueOf(saved.getId()),
                "\u521b\u5efa\u6d3b\u52a8\uff1a" + saved.getTitle()
                        + "\uff0c\u5f00\u59cb\u65f6\u95f4\uff1a"
                        + saved.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        + "\uff0c\u7ed3\u675f\u65f6\u95f4\uff1a"
                        + saved.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
        return saved;
    }

    public ParticipationRecord enroll(Long activityId, Long userId) {
        VolunteerActivity activity = getActivity(activityId);
        UserProfile user = getUser(userId);
        if (user.getRole() != UserRole.VOLUNTEER) {
            throw new IllegalArgumentException("\u53ea\u6709\u5fd7\u613f\u8005\u624d\u80fd\u62a5\u540d\u6d3b\u52a8");
        }
        identityVerificationService.ensureVerified(user, "\u62a5\u540d\u6d3b\u52a8");
        if (activity.getEnrolledCount() >= activity.getCapacity()) {
            throw new IllegalArgumentException("\u6d3b\u52a8\u540d\u989d\u5df2\u6ee1");
        }
        if (participationRecordRepository.existsByActivityIdAndUserId(activityId, userId)) {
            throw new IllegalArgumentException("\u4f60\u5df2\u7ecf\u62a5\u540d\u8fc7\u8be5\u6d3b\u52a8");
        }

        ParticipationRecord record = new ParticipationRecord();
        record.setActivityId(activityId);
        record.setUserId(userId);
        record.setStatus(ParticipationStatus.PENDING);
        record.setCreatedAt(LocalDateTime.now());
        ParticipationRecord saved = participationRecordRepository.save(record);

        activity.setEnrolledCount(activity.getEnrolledCount() + 1);
        volunteerActivityRepository.save(activity);
        notificationService.sendEnrollmentSubmitted(user, activity, saved.getId());
        notificationService.sendEnrollmentPendingReview(activity, user, saved.getId());
        return saved;
    }

    public ParticipationRecord checkIn(Long activityId, Long userId, CheckInRequest request) {
        VolunteerActivity activity = getActivity(activityId);
        ParticipationRecord record = findRecord(activityId, userId);
        UserProfile user = getUser(userId);
        if (record.getStatus() == ParticipationStatus.PENDING) {
            throw new IllegalArgumentException("\u62a5\u540d\u5f85\u5ba1\u6838\uff0c\u6682\u65f6\u4e0d\u80fd\u7b7e\u5230");
        }
        if (record.getStatus() == ParticipationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("\u5f53\u524d\u5df2\u7b7e\u5230\uff0c\u65e0\u9700\u91cd\u590d\u7b7e\u5230");
        }
        if (record.getStatus() == ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u5f53\u524d\u6d3b\u52a8\u5df2\u7ecf\u7b7e\u9000\u5b8c\u6210");
        }

        validateCheckIn(activity, request);
        record.setStatus(ParticipationStatus.CHECKED_IN);
        record.setCheckInTime(LocalDateTime.now());
        record.setCheckInMethod(request.getMethod());
        record.setLastLatitude(maskCoordinate(request.getLatitude()));
        record.setLastLongitude(maskCoordinate(request.getLongitude()));
        ParticipationRecord saved = participationRecordRepository.save(record);
        saveTrackPoint(activityId, userId, request.getLatitude(), request.getLongitude(), record.getCheckInTime(), TrackPointType.START, true);
        saveBlockchainProof(
                saved,
                activity,
                user,
                BlockchainEventType.CHECK_IN,
                buildGenericHash("CHECK_IN|" + saved.getId() + "|" + saved.getCheckInTime() + "|" + request.getMethod()
                        + "|" + maskCoordinate(request.getLatitude()) + "|" + maskCoordinate(request.getLongitude())),
                "\u7b7e\u5230\u5b58\u8bc1\uff0c\u65b9\u5f0f\uff1a" + request.getMethod()
                        + "\uff0c\u5750\u6807\uff1a" + maskCoordinate(request.getLatitude()) + ", " + maskCoordinate(request.getLongitude()),
                saved.getCheckInTime()
        );
        return saved;
    }

    public ParticipationRecord checkOut(Long activityId, Long userId, CheckOutRequest request) {
        VolunteerActivity activity = getActivity(activityId);
        ParticipationRecord record = findRecord(activityId, userId);
        if (record.getStatus() != ParticipationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("\u8bf7\u5148\u7b7e\u5230\u518d\u7b7e\u9000");
        }
        if (request.getLatitude() != null && request.getLongitude() != null
                && !isWithinGeofence(activity, request.getLatitude(), request.getLongitude())) {
            throw new IllegalArgumentException("\u5f53\u524d\u4f4d\u7f6e\u8d85\u51fa\u6d3b\u52a8\u670d\u52a1\u8303\u56f4\uff0c\u65e0\u6cd5\u7b7e\u9000");
        }

        UserProfile user = getUser(userId);
        return completeRecord(record, activity, user, LocalDateTime.now(), request.getLatitude(), request.getLongitude());
    }

    public String refreshCheckInCode(Long activityId) {
        ensureVerifiedOperator(AuthContext.getUserId(), "\u5237\u65b0\u7b7e\u5230\u7801");
        VolunteerActivity activity = getActivity(activityId);
        String code = generateCode();
        activity.setCheckInCode(code);
        volunteerActivityRepository.save(activity);
        checkInCodeCacheService.cacheCode(activityId, code);
        adminAuditLogService.log(
                AuthContext.getUserId(),
                "CHECK_IN_CODE_REFRESHED",
                "ACTIVITY",
                String.valueOf(activityId),
                "refresh check-in code: " + activity.getTitle()
        );
        return code;
    }

    @Transactional(readOnly = true)
    public UserDashboardView getDashboard(Long userId) {
        UserProfile user = getUser(userId);
        List<ParticipationRecord> records = participationRecordRepository.findByUserIdOrderByIdDesc(userId);
        enrichParticipationRecords(records);
        List<ParticipationRecord> effectiveRecords = records.stream()
                .filter(this::isEffectiveParticipationRecord)
                .collect(Collectors.toList());
        return new UserDashboardView(user, records);
    }

    @Transactional(readOnly = true)
    public VolunteerAnnualReportView getAnnualReport(Long userId, Integer year) {
        UserProfile user = getUser(userId);
        if (user == null || (user.getRole() != UserRole.VOLUNTEER
                && user.getRole() != UserRole.ADMIN
                && user.getRole() != UserRole.SUPER_ADMIN)) {
            throw new IllegalArgumentException("\u53ea\u6709\u5fd7\u613f\u8005\u3001\u6d3b\u52a8\u7ba1\u7406\u5458\u548c\u8d85\u7ea7\u7ba1\u7406\u5458\u53ef\u4ee5\u67e5\u770b\u7231\u5fc3\u5e74\u62a5");
        }
        return buildAnnualReport(user, year);
    }

    @Transactional(readOnly = true)
    public List<AdminMonitoredUserView> listMonitoredUsers() {
        Map<Long, Integer> activityCountMap = participationRecordRepository.findAll().stream()
                .filter(this::isEffectiveParticipationRecord)
                .collect(Collectors.groupingBy(
                        ParticipationRecord::getUserId,
                        Collectors.collectingAndThen(
                                Collectors.mapping(ParticipationRecord::getActivityId, Collectors.toSet()),
                                Set::size
                        )
                ));

        return userProfileRepository.findAll().stream()
                .filter(this::isMonitorableUser)
                .sorted(Comparator.comparing(UserProfile::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(UserProfile::getId, Comparator.reverseOrder()))
                .map(user -> {
                    AdminMonitoredUserView view = new AdminMonitoredUserView();
                    view.setId(user.getId());
                    view.setName(resolveDisplayName(user));
                    view.setAvatarUrl(user.getAvatarUrl());
                    view.setRole(user.getRole());
                    view.setVerified(Boolean.TRUE.equals(user.getVerified()));
                    view.setBadgeName(user.getBadgeName());
                    view.setVolunteerCardNo(user.getVolunteerCardNo());
                    view.setTotalHours(user.getTotalHours());
                    view.setTotalPoints(user.getTotalPoints());
                    view.setCreditScore(user.getCreditScore());
                    view.setActivityCount(activityCountMap.getOrDefault(user.getId(), 0));
                    view.setCreatedAt(user.getCreatedAt());
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminUserAnalyticsView getUserAnalytics(Long userId, Integer year) {
        UserProfile user = getUser(userId);
        if (!isMonitorableUser(user)) {
            throw new IllegalArgumentException("\u5f53\u524d\u7528\u6237\u4e0d\u652f\u6301\u67e5\u770b\u5206\u6790\u753b\u50cf");
        }
        List<ParticipationRecord> records = participationRecordRepository.findByUserIdOrderByIdDesc(userId);
        enrichParticipationRecords(records);
        List<ParticipationRecord> effectiveRecords = records.stream()
                .filter(this::isEffectiveParticipationRecord)
                .collect(Collectors.toList());

        AdminUserAnalyticsView view = new AdminUserAnalyticsView();
        view.setUserId(user.getId());
        view.setUserName(resolveDisplayName(user));
        view.setAvatarUrl(user.getAvatarUrl());
        view.setRole(user.getRole());
        view.setVerified(Boolean.TRUE.equals(user.getVerified()));
        view.setRealName(user.getRealName());
        view.setMaskedIdCardNo(maskIdCardNo(user.getIdCardNo()));
        view.setVolunteerCardNo(user.getVolunteerCardNo());
        view.setBadgeName(user.getBadgeName());
        view.setTotalHours(user.getTotalHours());
        view.setTotalPoints(user.getTotalPoints());
        view.setCreditScore(user.getCreditScore());
        view.setCreatedAt(user.getCreatedAt());
        view.setLeaderboardRank(resolveLeaderboardRank(user.getId()));
        view.setAnnualReport(buildAnnualReport(user, year));
        view.setParticipationRecords(effectiveRecords);
        view.setRelatedHeatmapActivities(buildRelatedHeatmapActivities(view.getAnnualReport()));
        view.setEvidenceSummary(buildUserEvidenceSummary(user, effectiveRecords));
        return view;
    }

    private VolunteerAnnualReportView buildAnnualReport(UserProfile user, Integer year) {
        int targetYear = resolveTargetYear(year);
        List<ParticipationRecord> allRecords = participationRecordRepository.findByUserIdOrderByIdDesc(user.getId());
        enrichParticipationRecords(allRecords);
        Map<Long, VolunteerActivity> activityMap = loadActivityMap(allRecords);
        List<ParticipationRecord> annualRecords = allRecords.stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> isInYear(resolveRecordTime(record), targetYear))
                .collect(Collectors.toList());

        VolunteerAnnualReportView view = new VolunteerAnnualReportView();
        view.setYear(targetYear);
        view.setTotalServiceHours(round(annualRecords.stream().mapToDouble(ParticipationRecord::getServiceHours).sum()));
        view.setTotalActivities((int) annualRecords.stream().map(ParticipationRecord::getActivityId).distinct().count());
        view.setTotalPoints(annualRecords.stream().mapToInt(ParticipationRecord::getEarnedPoints).sum());
        view.setTotalCertificates((int) annualRecords.stream()
                .filter(record -> StringUtils.hasText(record.getCertificateNo()) || StringUtils.hasText(record.getCertificateUrl()))
                .count());

        ParticipationRecord bestRecord = annualRecords.stream()
                .filter(record -> record.getServiceRating() != null)
                .max(Comparator.comparing(ParticipationRecord::getServiceRating)
                        .thenComparing(ParticipationRecord::getServiceHours))
                .orElse(null);
        if (bestRecord != null) {
            view.setBestActivityTitle(bestRecord.getActivityTitle());
            view.setBestActivityRating(bestRecord.getServiceRating());
        }

        view.setTopCategory(resolveTopCategory(annualRecords));
        view.setCoveredAreas(resolveCoveredAreas(annualRecords, activityMap));
        view.setFootprints(buildAnnualFootprints(annualRecords, activityMap));
        view.setRepresentativeRecords(buildRepresentativeRecords(annualRecords, activityMap, user));
        view.setSummaryText(buildAnnualSummary(view));
        return view;
    }

    @Transactional(readOnly = true)
    public AdminAnnualReportView getAdminAnnualReport(Long adminUserId, Integer year) {
        UserProfile admin = getUser(adminUserId);
        if (admin.getRole() != UserRole.ADMIN && admin.getRole() != UserRole.SUPER_ADMIN) {
            throw new IllegalArgumentException("\u53ea\u6709\u7ba1\u7406\u5458\u548c\u8d85\u7ea7\u7ba1\u7406\u5458\u53ef\u4ee5\u67e5\u770b\u7ba1\u7406\u5e74\u62a5");
        }

        int targetYear = resolveTargetYear(year);
        List<VolunteerActivity> managedActivities = listManagedActivities(adminUserId, targetYear);
        List<ParticipationRecord> allRecords = participationRecordRepository.findAll();
        List<ParticipationRecord> managedRecords = managedActivities.isEmpty()
                ? new ArrayList<>()
                : allRecords.stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> isInYear(resolveRecordTime(record), targetYear))
                .filter(record -> containsActivity(managedActivities, record.getActivityId()))
                .collect(Collectors.toList());
        enrichParticipationRecords(managedRecords);

        AdminAnnualReportView view = new AdminAnnualReportView();
        view.setYear(targetYear);
        view.setAdminUserId(admin.getId());
        view.setAdminName(resolveDisplayName(admin));
        view.setTotalActivities(managedActivities.size());
        view.setTotalServiceCount(managedRecords.size());
        view.setTotalServiceHours(round(managedRecords.stream().mapToDouble(ParticipationRecord::getServiceHours).sum()));
        view.setCoveredCommunities(buildTopAreas(managedActivities, Integer.MAX_VALUE));
        view.setCoveredCommunityCount(view.getCoveredCommunities().size());
        view.setActiveMonths((int) managedActivities.stream()
                .filter(activity -> activity.getStartTime() != null)
                .map(activity -> activity.getStartTime().getMonthValue())
                .distinct()
                .count());
        view.setHottestCommunity(view.getCoveredCommunities().isEmpty() ? "" : view.getCoveredCommunities().get(0).getLabel());
        view.setTopCategory(resolveTopCategoryFromActivities(managedActivities));
        view.setCategoryStats(buildCategoryStats(managedActivities));
        view.setMonthlyActivityTrends(buildMonthlyActivityTrends(managedActivities, targetYear));
        view.setMonthlyServiceHourTrends(buildMonthlyServiceHourTrends(managedRecords, targetYear));
        view.setCommunityHeatmap(buildCommunityHeatmap(managedActivities));
        view.setActivityHeatmap(buildActivityHeatmap(managedActivities));
        view.setRepresentativeRecords(buildRepresentativeManagedRecords(managedRecords, admin));
        view.setSummaryText(buildAdminAnnualSummary(view));
        return view;
    }

    @Transactional(readOnly = true)
    public AdminOverview getAdminOverview(Long operatorId) {
        int completedRecords = Math.toIntExact(participationRecordRepository.countByStatus(ParticipationStatus.COMPLETED));
        int totalPointsIssued = participationRecordRepository.findAll().stream()
                .mapToInt(ParticipationRecord::getEarnedPoints)
                .sum();
        int totalVolunteers = Math.toIntExact(userProfileRepository.countByRole(UserRole.VOLUNTEER));
        int totalActivities = Math.toIntExact(volunteerActivityRepository.count());
        int pendingSupplements = listPendingSupplements(operatorId).size();
        int pendingAnomalies = Math.toIntExact(listAnomalyRecords(operatorId).stream()
                .filter(record -> record.getAnomalyStatus() == AnomalyStatus.PENDING)
                .count());
        int pendingRedemptions = Math.toIntExact(redemptionRecordRepository.findAll().stream()
                .filter(record -> record.getStatus() == RedemptionStatus.CREATED)
                .count());
        int lowStockItems = Math.toIntExact(storeItemRepository.findAll().stream()
                .filter(StoreItem::isActive)
                .filter(item -> item.getStock() <= LOW_STOCK_THRESHOLD)
                .count());

        AdminOverview overview = new AdminOverview(totalActivities, totalVolunteers, completedRecords, totalPointsIssued);
        overview.setPendingSupplements(pendingSupplements);
        overview.setPendingAnomalies(pendingAnomalies);
        overview.setPendingRedemptions(pendingRedemptions);
        overview.setLowStockItems(lowStockItems);
        return overview;
    }

    @Transactional(readOnly = true)
    public AnnualPlatformOverviewView getAnnualPlatformOverview(Integer year) {
        int targetYear = resolveTargetYear(year);
        List<VolunteerActivity> annualActivities = volunteerActivityRepository.findAll().stream()
                .filter(activity -> isInYear(activity.getStartTime(), targetYear))
                .collect(Collectors.toList());
        List<ParticipationRecord> annualRecords = participationRecordRepository.findAll().stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> isInYear(resolveRecordTime(record), targetYear))
                .collect(Collectors.toList());

        AnnualPlatformOverviewView view = new AnnualPlatformOverviewView();
        view.setYear(targetYear);
        view.setTotalActivities(annualActivities.size());
        view.setTotalServiceHours(round(annualRecords.stream().mapToDouble(ParticipationRecord::getServiceHours).sum()));
        view.setActiveVolunteers((int) annualRecords.stream().map(ParticipationRecord::getUserId).distinct().count());
        view.setActiveAdmins((int) annualActivities.stream()
                .map(VolunteerActivity::getOrganizerId)
                .filter(item -> item != null)
                .distinct()
                .count());
        view.setTotalServiceCount(annualRecords.size());
        view.setHotAreas(buildTopAreas(annualActivities, 5));
        view.setCategoryStats(buildCategoryStats(annualActivities));
        view.setMonthlyActivityTrends(buildMonthlyActivityTrends(annualActivities, targetYear));
        view.setMonthlyServiceHourTrends(buildMonthlyServiceHourTrends(annualRecords, targetYear));
        view.setCommunityHeatmap(buildCommunityHeatmap(annualActivities));
        view.setActivityHeatmap(buildActivityHeatmap(annualActivities));
        view.setAdminLeaderboard(listAdminLeaderboard(targetYear));
        view.setVolunteerLeaderboard(listLeaderboard());
        return view;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardView> listLeaderboardForAdmin() {
        return listLeaderboard();
    }

    @Transactional(readOnly = true)
    public List<HeatmapPointView> buildSmartHeatmapForAdmin() {
        return buildSmartHeatmap();
    }

    @Transactional(readOnly = true)
    public List<StarLeaderboardView> listStarLeaderboard(String period) {
        return buildStarLeaderboard(resolveParticipantUsers(), volunteerActivityRepository.findAll(), period);
    }

    @Transactional(readOnly = true)
    public List<StarLeaderboardView> listAdminStarLeaderboard(Long adminUserId, String period) {
        List<VolunteerActivity> managedActivities = listManagedActivities(adminUserId, null);
        return buildStarLeaderboard(resolveParticipantUsers(), managedActivities, period);
    }

    @Transactional(readOnly = true)
    public List<StarLeaderboardView> listPlatformStarLeaderboard(String period) {
        return buildStarLeaderboard(resolveParticipantUsers(), volunteerActivityRepository.findAll(), period);
    }

    @Transactional(readOnly = true)
    public List<CommunityInsightView> listCommunityInsights(String period) {
        return buildCommunityInsights(volunteerActivityRepository.findAll(), period);
    }

    @Transactional(readOnly = true)
    public List<CommunityInsightView> listAdminCommunityInsights(Long adminUserId, String period) {
        return buildCommunityInsights(listManagedActivities(adminUserId, null), period);
    }

    @Transactional(readOnly = true)
    public List<CommunityInsightView> listPlatformCommunityInsights(String period) {
        return buildCommunityInsights(volunteerActivityRepository.findAll(), period);
    }

    @Transactional(readOnly = true)
    public List<AdminLeaderboardView> listAdminLeaderboard(Integer year) {
        int targetYear = resolveTargetYear(year);
        Map<Long, UserProfile> adminMap = userProfileRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.SUPER_ADMIN)
                .collect(Collectors.toMap(UserProfile::getId, user -> user));

        return adminMap.values().stream()
                .map(admin -> buildAdminLeaderboardView(admin, targetYear))
                .filter(item -> item.getTotalActivities() > 0 || item.getTotalServiceCount() > 0)
                .sorted(Comparator.comparing(AdminLeaderboardView::getTotalActivities).reversed()
                        .thenComparing(AdminLeaderboardView::getTotalServiceHours).reversed()
                        .thenComparing(AdminLeaderboardView::getTotalServiceCount).reversed()
                        .thenComparing(AdminLeaderboardView::getUserId))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HeatmapPointView> buildAdminCommunityHeatmap(Long adminUserId, Integer year) {
        return buildCommunityHeatmap(listManagedActivities(adminUserId, resolveTargetYear(year)));
    }

    @Transactional(readOnly = true)
    public List<HeatmapPointView> buildAdminActivityHeatmap(Long adminUserId, Integer year) {
        return buildActivityHeatmap(listManagedActivities(adminUserId, resolveTargetYear(year)));
    }

    @Transactional(readOnly = true)
    public List<ActivityRecordView> listActivityRecords(Long activityId, Long operatorId) {
        VolunteerActivity activity = getManageableActivity(activityId, operatorId);
        return participationRecordRepository.findByActivityIdOrderByIdDesc(activityId).stream()
                .map(record -> buildActivityRecordView(record, activity, getUser(record.getUserId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceCertificateView getServiceCertificate(Long recordId, Long operatorId) {
        ensureVerifiedOperator(operatorId, "\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u670d\u52a1\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u5f53\u524d\u670d\u52a1\u8bb0\u5f55\u5c1a\u672a\u5b8c\u6210\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }
        if (!record.isOrganizerConfirmed()) {
            throw new IllegalArgumentException("\u7ec4\u7ec7\u8005\u5c1a\u672a\u786e\u8ba4\u8be5\u670d\u52a1\u8bb0\u5f55\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }
        if (record.getRewardFinalizedAt() == null) {
            throw new IllegalArgumentException("\u79ef\u5206\u4e0e\u8bc1\u4e66\u5c1a\u672a\u7ed3\u7b97\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }

        VolunteerActivity activity = getManageableActivity(record.getActivityId(), operatorId);
        UserProfile user = getUser(record.getUserId());
        ServiceCertificateView view = new ServiceCertificateView();
        view.setRecordId(record.getId());
        view.setActivityId(activity.getId());
        view.setUserId(user.getId());
        view.setCertificateTitle(systemSettingService.getCertificateTitle());
        view.setSystemName(systemSettingService.getSystemName());
        view.setVolunteerName(resolveCertificateVolunteerName(user));
        view.setVolunteerCardNo(user.getVolunteerCardNo());
        view.setMaskedIdCardNo(maskIdCardNo(user.getIdCardNo()));
        view.setActivityTitle(activity.getTitle());
        view.setActivityCategory(normalizeActivityCategory(activity.getCategory()));
        view.setActivityLocation(activity.getLocation());
        view.setActivityStartTime(activity.getStartTime());
        view.setActivityEndTime(activity.getEndTime());
        view.setCheckInTime(record.getCheckInTime());
        view.setCheckOutTime(record.getCheckOutTime());
        view.setServiceHours(record.getServiceHours());
        view.setServiceRating(record.getServiceRating());
        view.setEarnedPoints(record.getEarnedPoints());
        view.setServiceComment(record.getServiceComment());
        view.setOrganizerConfirmedAt(record.getOrganizerConfirmedAt());
        view.setOrganizerConfirmComment(record.getOrganizerConfirmComment());
        view.setCertificateNo(record.getCertificateNo());
        view.setCertificateUrl(record.getCertificateUrl());
        view.setRewardFinalizedAt(record.getRewardFinalizedAt());
        view.setIssuedAt(record.getRewardFinalizedAt());
        view.setEvidenceHash(record.getEvidenceHash());
        applyLatestBlockchainProof(view, record.getId());
        view.setSnapshots(serviceSnapshotRepository
                .findByActivityIdAndUserIdOrderByCreatedAtDesc(activity.getId(), user.getId())
                .stream()
                .limit(3)
                .map(this::buildCertificateSnapshotView)
                .collect(Collectors.toList()));
        return view;
    }

    @Transactional(readOnly = true)
    public ServiceCertificateView getMyServiceCertificate(Long recordId, Long userId) {
        UserProfile currentUser = getUser(userId);
        ensureActivityParticipant(currentUser);
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u670d\u52a1\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (!record.getUserId().equals(userId)) {
            throw new IllegalArgumentException("\u4f60\u53ea\u80fd\u5bfc\u51fa\u81ea\u5df1\u7684\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }
        if (record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u5f53\u524d\u670d\u52a1\u8bb0\u5f55\u5c1a\u672a\u5b8c\u6210\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }
        if (!record.isOrganizerConfirmed()) {
            throw new IllegalArgumentException("\u7ec4\u7ec7\u8005\u5c1a\u672a\u786e\u8ba4\u8be5\u670d\u52a1\u8bb0\u5f55\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }
        if (record.getRewardFinalizedAt() == null) {
            throw new IllegalArgumentException("\u79ef\u5206\u4e0e\u8bc1\u4e66\u5c1a\u672a\u7ed3\u7b97\uff0c\u6682\u65f6\u4e0d\u80fd\u5bfc\u51fa\u5fd7\u613f\u670d\u52a1\u8bc1\u660e");
        }

        VolunteerActivity activity = getActivity(record.getActivityId());
        ServiceCertificateView view = new ServiceCertificateView();
        view.setRecordId(record.getId());
        view.setActivityId(activity.getId());
        view.setUserId(currentUser.getId());
        view.setCertificateTitle(systemSettingService.getCertificateTitle());
        view.setSystemName(systemSettingService.getSystemName());
        view.setVolunteerName(resolveCertificateVolunteerName(currentUser));
        view.setVolunteerCardNo(currentUser.getVolunteerCardNo());
        view.setMaskedIdCardNo(maskIdCardNo(currentUser.getIdCardNo()));
        view.setActivityTitle(activity.getTitle());
        view.setActivityCategory(normalizeActivityCategory(activity.getCategory()));
        view.setActivityLocation(activity.getLocation());
        view.setActivityStartTime(activity.getStartTime());
        view.setActivityEndTime(activity.getEndTime());
        view.setCheckInTime(record.getCheckInTime());
        view.setCheckOutTime(record.getCheckOutTime());
        view.setServiceHours(record.getServiceHours());
        view.setServiceRating(record.getServiceRating());
        view.setEarnedPoints(record.getEarnedPoints());
        view.setServiceComment(record.getServiceComment());
        view.setOrganizerConfirmedAt(record.getOrganizerConfirmedAt());
        view.setOrganizerConfirmComment(record.getOrganizerConfirmComment());
        view.setCertificateNo(record.getCertificateNo());
        view.setCertificateUrl(record.getCertificateUrl());
        view.setRewardFinalizedAt(record.getRewardFinalizedAt());
        view.setIssuedAt(record.getRewardFinalizedAt());
        view.setEvidenceHash(record.getEvidenceHash());
        applyLatestBlockchainProof(view, record.getId());
        view.setSnapshots(serviceSnapshotRepository
                .findByActivityIdAndUserIdOrderByCreatedAtDesc(activity.getId(), currentUser.getId())
                .stream()
                .limit(3)
                .map(this::buildCertificateSnapshotView)
                .collect(Collectors.toList()));
        return view;
    }

    @Transactional(readOnly = true)
    public byte[] getServiceCertificatePdf(Long recordId, Long operatorId) {
        return serviceCertificatePdfService.generatePdf(getServiceCertificate(recordId, operatorId));
    }

    @Transactional(readOnly = true)
    public byte[] getMyServiceCertificatePdf(Long recordId, Long userId) {
        return serviceCertificatePdfService.generatePdf(getMyServiceCertificate(recordId, userId));
    }

    @Transactional(readOnly = true)
    public List<ActivityRecordView> listPendingEnrollments(Long operatorId) {
        UserProfile operator = getUser(operatorId);
        return participationRecordRepository.findByStatusOrderByIdDesc(ParticipationStatus.PENDING).stream()
                .filter(record -> canManageActivity(record.getActivityId(), operator))
                .map(record -> buildActivityRecordView(
                        record,
                        getActivity(record.getActivityId()),
                        getUser(record.getUserId())
                ))
                .collect(Collectors.toList());
    }

    public ActivityRecordView reviewEnrollment(Long recordId, Long reviewerId, boolean approved) {
        ensureVerifiedOperator(reviewerId, "\u5ba1\u6838\u6d3b\u52a8\u62a5\u540d");
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u62a5\u540d\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (record.getStatus() != ParticipationStatus.PENDING) {
            throw new IllegalArgumentException("\u5f53\u524d\u62a5\u540d\u8bb0\u5f55\u65e0\u9700\u91cd\u590d\u5ba1\u6838");
        }

        VolunteerActivity activity = getManageableActivity(record.getActivityId(), reviewerId);
        UserProfile user = getUser(record.getUserId());
        UserProfile reviewer = getUser(reviewerId);
        if (approved) {
            record.setStatus(ParticipationStatus.APPROVED);
            ParticipationRecord saved = participationRecordRepository.save(record);
            notificationService.sendEnrollmentReviewed(user, activity, saved.getId(), true);
            notificationService.sendEnrollmentReviewedOperator(reviewer, user, activity, saved.getId(), true);
            adminAuditLogService.log(
                    reviewerId,
                    "ENROLLMENT_APPROVED",
                    "PARTICIPATION_RECORD",
                    String.valueOf(saved.getId()),
                    "approve enrollment: " + activity.getTitle() + ", user: " + resolveDisplayName(user)
            );
            return buildActivityRecordView(saved, activity, user);
        }

        participationRecordRepository.delete(record);
        activity.setEnrolledCount(Math.max(0, activity.getEnrolledCount() - 1));
        volunteerActivityRepository.save(activity);
        notificationService.sendEnrollmentReviewed(user, activity, record.getId(), false);
        notificationService.sendEnrollmentReviewedOperator(reviewer, user, activity, record.getId(), false);
        adminAuditLogService.log(
                reviewerId,
                "ENROLLMENT_REJECTED",
                "PARTICIPATION_RECORD",
                String.valueOf(record.getId()),
                "reject enrollment: " + activity.getTitle() + ", user: " + resolveDisplayName(user)
        );
        return buildActivityRecordView(record, activity, user);
    }

    @Transactional(readOnly = true)
    public List<ActivityRecordView> listAnomalyRecords(Long operatorId) {
        UserProfile operator = getUser(operatorId);
        return participationRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(ParticipationRecord::getId, Comparator.reverseOrder()))
                .filter(record -> canManageActivity(record.getActivityId(), operator))
                .map(record -> buildActivityRecordView(
                        record,
                        getActivity(record.getActivityId()),
                        getUser(record.getUserId())
                ))
                .filter(this::isAnomalyRecord)
                .collect(Collectors.toList());
    }

    public ActivityRecordView updateAnomalyStatus(Long recordId, Long operatorId, AnomalyStatus status, String remark) {
        ensureVerifiedOperator(operatorId, "\u5904\u7406\u5f02\u5e38\u8bb0\u5f55");
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u5f02\u5e38\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (status == null || status == AnomalyStatus.PENDING) {
            throw new IllegalArgumentException("\u8bf7\u9009\u62e9\u6709\u6548\u7684\u5f02\u5e38\u5904\u7f6e\u72b6\u6001");
        }

        String sanitizedRemark = remark == null ? "" : remark.trim();
        if (sanitizedRemark.isEmpty()) {
            throw new IllegalArgumentException("\u5904\u7f6e\u5907\u6ce8\u4e0d\u80fd\u4e3a\u7a7a");
        }

        VolunteerActivity activity = getManageableActivity(record.getActivityId(), operatorId);
        UserProfile user = getUser(record.getUserId());
        ActivityRecordView currentView = buildActivityRecordView(record, activity, user);
        if (!isAnomalyRecord(currentView)) {
            throw new IllegalArgumentException("\u5f53\u524d\u8bb0\u5f55\u4e0d\u5c5e\u4e8e\u5f85\u5904\u7f6e\u7684\u5f02\u5e38\u8bb0\u5f55");
        }

        record.setAnomalyStatus(status);
        record.setAnomalyRemark(sanitizedRemark);
        ParticipationRecord saved = participationRecordRepository.save(record);
        adminAuditLogService.log(
                operatorId,
                "ANOMALY_STATUS_UPDATED",
                "PARTICIPATION_RECORD",
                String.valueOf(saved.getId()),
                "\u5c06\u5f02\u5e38\u8bb0\u5f55 " + saved.getId() + " \u66f4\u65b0\u4e3a " + status + "\uff0c\u5907\u6ce8\uff1a" + sanitizedRemark
        );
        return buildActivityRecordView(saved, activity, user);
    }

    public ServiceSnapshot createSnapshot(Long userId, CreateSnapshotRequest request) {
        ParticipationRecord record = findRecord(request.getActivityId(), userId);
        if (record.getStatus() != ParticipationStatus.CHECKED_IN && record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u8bf7\u5148\u7b7e\u5230\u540e\u518d\u4e0a\u4f20\u670d\u52a1\u5feb\u7167");
        }
        String imageUrl = normalizeSnapshotImageUrl(request.getImageUrl());
        ServiceSnapshot snapshot = new ServiceSnapshot();
        snapshot.setActivityId(request.getActivityId());
        snapshot.setUserId(userId);
        snapshot.setImageUrl(imageUrl);
        snapshot.setNote(request.getNote());
        snapshot.setCreatedAt(LocalDateTime.now());
        ServiceSnapshot saved = serviceSnapshotRepository.save(snapshot);
        VolunteerActivity activity = getActivity(request.getActivityId());
        UserProfile user = getUser(userId);
        saveBlockchainProof(
                record,
                activity,
                user,
                BlockchainEventType.SNAPSHOT_UPLOAD,
                buildGenericHash("SNAPSHOT|" + record.getId() + "|" + saved.getId() + "|" + saved.getCreatedAt() + "|" + saved.getImageUrl()),
                "\u4e0a\u4f20\u670d\u52a1\u5feb\u7167\uff0c\u5feb\u7167\u7f16\u53f7\uff1a" + saved.getId(),
                saved.getCreatedAt()
        );
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ServiceSnapshot> listSnapshots(Long activityId, Long userId) {
        getActivity(activityId);
        return serviceSnapshotRepository.findByActivityIdAndUserIdOrderByCreatedAtDesc(activityId, userId);
    }

    @Transactional(readOnly = true)
    public List<ServiceSnapshot> listSnapshotsForAdmin(Long activityId, Long operatorId) {
        getManageableActivity(activityId, operatorId);
        return serviceSnapshotRepository.findByActivityIdOrderByCreatedAtDesc(activityId);
    }

    public TrackPoint recordTrackPoint(Long userId, TrackPointRequest request) {
        ParticipationRecord record = findRecord(request.getActivityId(), userId);
        if (record.getStatus() != ParticipationStatus.CHECKED_IN && record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u8bf7\u5148\u7b7e\u5230\u540e\u518d\u8bb0\u5f55\u8f68\u8ff9");
        }
        TrackPointType pointType = request.getPointType() == null ? TrackPointType.MIDDLE : request.getPointType();
        return saveTrackPoint(
                request.getActivityId(),
                userId,
                request.getLatitude(),
                request.getLongitude(),
                request.getRecordedAt() == null ? LocalDateTime.now() : request.getRecordedAt(),
                pointType,
                pointType != TrackPointType.MIDDLE
        );
    }

    @Transactional(readOnly = true)
    public List<TrackPoint> listTrackPoints(Long activityId, Long userId) {
        getActivity(activityId);
        return trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(activityId, userId);
    }

    @Transactional(readOnly = true)
    public List<TrackPoint> listTrackPointsForAdmin(Long activityId, Long operatorId) {
        getManageableActivity(activityId, operatorId);
        return trackPointRepository.findByActivityIdOrderByRecordedAtAsc(activityId);
    }

    @Transactional(readOnly = true)
    public List<BlockchainProofView> listBlockchainProofsForAdmin(Long activityId, Long operatorId) {
        getManageableActivity(activityId, operatorId);
        return blockchainProofRecordRepository.findByActivityIdOrderByEventTimeAscCreatedAtAsc(activityId).stream()
                .map(this::buildBlockchainProofView)
                .collect(Collectors.toList());
    }

    public int backfillBlockchainProofsForAdmin(Long activityId) {
        ensureVerifiedOperator(AuthContext.getUserId(), "\u8865\u5f55\u533a\u5757\u94fe\u5b58\u8bc1");
        VolunteerActivity activity = getManageableActivity(activityId, AuthContext.getUserId());
        List<ParticipationRecord> records = participationRecordRepository.findByActivityIdOrderByIdDesc(activityId);
        int createdCount = 0;
        for (ParticipationRecord record : records) {
            createdCount += backfillBlockchainProofsForRecord(activity, record);
        }
        adminAuditLogService.log(
                AuthContext.getUserId(),
                "BLOCKCHAIN_PROOFS_BACKFILLED",
                "ACTIVITY",
                String.valueOf(activityId),
                "backfill blockchain proofs, created=" + createdCount + ", activity=" + activity.getTitle()
        );
        return createdCount;
    }

    public SupplementApplication applySupplement(Long userId, SupplementApplyRequest request) {
        VolunteerActivity activity = getActivity(request.getActivityId());
        UserProfile user = getUser(userId);
        SupplementApplication application = new SupplementApplication();
        application.setActivityId(request.getActivityId());
        application.setUserId(userId);
        application.setType(request.getType());
        application.setRequestedTime(request.getRequestedTime());
        application.setReason(request.getReason());
        application.setEvidenceImageUrl(request.getEvidenceImageUrl());
        application.setStatus(SupplementStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());
        SupplementApplication saved = supplementApplicationRepository.save(application);
        notificationService.sendSupplementSubmitted(user, activity, saved);
        notificationService.sendSupplementPendingReview(activity, user, saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<SupplementApplication> listMySupplements(Long userId) {
        return supplementApplicationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<SupplementApplication> listPendingSupplements(Long operatorId) {
        UserProfile operator = getUser(operatorId);
        return supplementApplicationRepository.findByStatusOrderByCreatedAtDesc(SupplementStatus.PENDING).stream()
                .filter(application -> canManageActivity(application.getActivityId(), operator))
                .collect(Collectors.toList());
    }

    public SupplementApplication reviewSupplement(Long supplementId, Long reviewerId, boolean approved, String reviewComment) {
        ensureVerifiedOperator(reviewerId, "\u5ba1\u6838\u8865\u7b7e\u7533\u8bf7");
        SupplementApplication application = supplementApplicationRepository.findById(supplementId)
                .orElseThrow(() -> new IllegalArgumentException("\u8865\u7b7e\u7533\u8bf7\u4e0d\u5b58\u5728"));
        if (application.getStatus() != SupplementStatus.PENDING) {
            throw new IllegalArgumentException("\u5f53\u524d\u8865\u7b7e\u7533\u8bf7\u5df2\u5ba1\u6838");
        }

        VolunteerActivity activity = getManageableActivity(application.getActivityId(), reviewerId);
        application.setReviewerId(reviewerId);
        application.setReviewComment(reviewComment);
        application.setReviewedAt(LocalDateTime.now());
        application.setStatus(approved ? SupplementStatus.APPROVED : SupplementStatus.REJECTED);
        supplementApplicationRepository.save(application);
        UserProfile user = getUser(application.getUserId());
        UserProfile reviewer = getUser(reviewerId);
        notificationService.sendSupplementReviewed(user, activity, application);
        notificationService.sendSupplementReviewedOperator(reviewer, user, activity, application);
        adminAuditLogService.log(
                reviewerId,
                "SUPPLEMENT_REVIEWED",
                "SUPPLEMENT",
                String.valueOf(application.getId()),
                "\u5ba1\u6838\u8865\u7b7e\u7533\u8bf7\uff0c\u6d3b\u52a8ID\uff1a" + application.getActivityId() + "\uff0c\u72b6\u6001\uff1a" + application.getStatus()
        );

        if (!approved) {
            return application;
        }

        ParticipationRecord record = participationRecordRepository.findByActivityIdAndUserId(application.getActivityId(), application.getUserId())
                .orElseGet(() -> {
                    ParticipationRecord created = new ParticipationRecord();
                    created.setActivityId(application.getActivityId());
                    created.setUserId(application.getUserId());
                    created.setStatus(ParticipationStatus.APPROVED);
                    created.setCreatedAt(LocalDateTime.now());
                    return participationRecordRepository.save(created);
                });

        if (application.getType() == SupplementType.CHECK_IN) {
            record.setStatus(ParticipationStatus.CHECKED_IN);
            record.setCheckInTime(application.getRequestedTime());
            record.setCheckInMethod(CheckInMethod.MANUAL);
            participationRecordRepository.save(record);
        } else {
            if (record.getCheckInTime() == null) {
                LocalDateTime fallbackCheckInTime = activity.getStartTime().isAfter(application.getRequestedTime())
                        ? activity.getStartTime()
                        : application.getRequestedTime().minusHours(2);
                record.setCheckInTime(fallbackCheckInTime);
            }
            record.setCheckInMethod(CheckInMethod.MANUAL);
            record.setStatus(ParticipationStatus.CHECKED_IN);
            completeRecord(record, activity, user, application.getRequestedTime(), record.getLastLatitude(), record.getLastLongitude());
        }
        return application;
    }

    public ActivityRecordView evaluateParticipation(Long recordId, Long reviewerId, int serviceRating, String serviceComment) {
        ensureVerifiedOperator(reviewerId, "\u8bc4\u4ef7\u670d\u52a1\u8bb0\u5f55");
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u670d\u52a1\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u53ea\u6709\u5df2\u5b8c\u6210\u7684\u670d\u52a1\u8bb0\u5f55\u624d\u80fd\u8bc4\u4ef7");
        }
        if (!record.isOrganizerConfirmed()) {
            throw new IllegalArgumentException("\u8bf7\u5148\u786e\u8ba4\u670d\u52a1\u5b8c\u6210\uff0c\u518d\u8fdb\u884c\u8bc4\u4ef7\u7ed3\u7b97");
        }
        if (record.getRewardFinalizedAt() != null || (record.getCertificateUrl() != null && !record.getCertificateUrl().trim().isEmpty())) {
            throw new IllegalArgumentException("\u5f53\u524d\u670d\u52a1\u8bb0\u5f55\u5df2\u7ecf\u5b8c\u6210\u79ef\u5206\u7ed3\u7b97");
        }

        VolunteerActivity activity = getManageableActivity(record.getActivityId(), reviewerId);
        UserProfile user = getUser(record.getUserId());
        UserProfile reviewer = getUser(reviewerId);
        int snapshotCount = serviceSnapshotRepository
                .findByActivityIdAndUserIdOrderByCreatedAtDesc(record.getActivityId(), record.getUserId())
                .size();
        int trackPointCount = trackPointRepository
                .findByActivityIdAndUserIdOrderByRecordedAtAsc(record.getActivityId(), record.getUserId())
                .size();

        RewardEngineService.RewardDecision rewardDecision = rewardEngineService.calculate(
                activity,
                record,
                snapshotCount,
                trackPointCount,
                serviceRating
        );

        record.setServiceRating(serviceRating);
        record.setServiceComment(serviceComment == null ? "" : serviceComment.trim());
        record.setEarnedPoints(rewardDecision.getFinalPoints());
        record.setPointBreakdown(rewardDecision.getSummary());
        record.setRewardFinalizedAt(LocalDateTime.now());

        String certificateNo = generateCertificateNo();
        CertificatePayload payload = new CertificatePayload();
        payload.setCertificateNo(certificateNo);
        payload.setVolunteerName(user.getName());
        payload.setActivityTitle(activity.getTitle());
        payload.setActivityLocation(activity.getLocation());
        payload.setServiceHours(record.getServiceHours());
        payload.setEarnedPoints(rewardDecision.getFinalPoints());
        payload.setIssueDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        String certificateUrl = certificateService.generateCertificate(payload);

        record.setCertificateNo(certificateNo);
        record.setCertificateUrl(certificateUrl);
        record.setEvidenceHash(buildEvidenceHash(record, activity, user));
        ParticipationRecord saved = participationRecordRepository.save(record);
        saveBlockchainProof(
                saved,
                activity,
                user,
                BlockchainEventType.REWARD_FINALIZE,
                saved.getEvidenceHash(),
                "\u670d\u52a1\u8bc4\u4ef7\u7ed3\u7b97\u5b58\u8bc1\uff0c\u8bc4\u5206\uff1a" + serviceRating + "\uff0c\u79ef\u5206\uff1a" + rewardDecision.getFinalPoints(),
                saved.getRewardFinalizedAt()
        );

        user.setTotalPoints(user.getTotalPoints() + rewardDecision.getFinalPoints());
        user.setCreditScore(Math.min(100, user.getCreditScore() + rewardDecision.getCreditDelta()));
        userProfileRepository.save(user);
        notificationService.sendServiceRatingReceived(user, activity, saved.getId(), serviceRating);
        notificationService.sendRewardFinalized(user, activity, saved.getId(), rewardDecision.getFinalPoints(), saved.getCertificateNo());
        notificationService.sendRewardFinalizedOperator(
                reviewer,
                user,
                activity,
                saved.getId(),
                rewardDecision.getFinalPoints(),
                serviceRating
        );

        adminAuditLogService.log(
                reviewerId,
                "PARTICIPATION_EVALUATED",
                "PARTICIPATION_RECORD",
                String.valueOf(saved.getId()),
                "evaluate participation, rating=" + serviceRating + ", points=" + rewardDecision.getFinalPoints()
        );
        return buildActivityRecordView(saved, activity, user);
    }

    public ActivityRecordView confirmParticipation(Long recordId, Long reviewerId) {
        ensureVerifiedOperator(reviewerId, "\u786e\u8ba4\u670d\u52a1\u5b8c\u6210");
        ParticipationRecord record = participationRecordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("\u670d\u52a1\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (record.getStatus() != ParticipationStatus.COMPLETED) {
            throw new IllegalArgumentException("\u53ea\u6709\u5df2\u5b8c\u6210\u7684\u670d\u52a1\u8bb0\u5f55\u624d\u80fd\u786e\u8ba4");
        }
        if (record.isOrganizerConfirmed()) {
            throw new IllegalArgumentException("\u5f53\u524d\u670d\u52a1\u8bb0\u5f55\u5df2\u786e\u8ba4\u5b8c\u6210");
        }
        if (record.getRewardFinalizedAt() != null) {
            throw new IllegalArgumentException("\u5f53\u524d\u670d\u52a1\u8bb0\u5f55\u5df2\u5b8c\u6210\u79ef\u5206\u7ed3\u7b97");
        }

        VolunteerActivity activity = getManageableActivity(record.getActivityId(), reviewerId);
        UserProfile user = getUser(record.getUserId());
        record.setOrganizerConfirmed(true);
        record.setOrganizerConfirmedAt(LocalDateTime.now());
        record.setOrganizerConfirmComment("\u7ba1\u7406\u5458\u786e\u8ba4\u670d\u52a1\u5b8c\u6210");
        ParticipationRecord saved = participationRecordRepository.save(record);
        saveBlockchainProof(
                saved,
                activity,
                user,
                BlockchainEventType.ORGANIZER_CONFIRM,
                buildGenericHash("ORGANIZER_CONFIRM|" + saved.getId() + "|" + saved.getOrganizerConfirmedAt()
                        + "|" + saved.getServiceHours()),
                "\u7ec4\u7ec7\u8005\u786e\u8ba4\u5b58\u8bc1\uff0c\u670d\u52a1\u65f6\u957f\uff1a" + saved.getServiceHours() + " \u5c0f\u65f6",
                saved.getOrganizerConfirmedAt()
        );

        adminAuditLogService.log(
                reviewerId,
                "PARTICIPATION_CONFIRMED",
                "PARTICIPATION_RECORD",
                String.valueOf(saved.getId()),
                "confirm participation: " + activity.getTitle() + ", user: " + resolveDisplayName(user)
        );
        return buildActivityRecordView(saved, activity, user);
    }

    @Transactional(readOnly = true)
    public List<StoreItem> listStoreItems() {
        return storeItemRepository.findByActiveTrueOrderByPointsCostAsc().stream()
                .sorted(Comparator.comparing(StoreItem::getPointsCost)
                        .thenComparing(StoreItem::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreItem> listStoreItemsForAdmin() {
        return storeItemRepository.findAll().stream()
                .sorted(Comparator.comparing(StoreItem::isActive).reversed()
                        .thenComparing(StoreItem::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public StoreItem createStoreItem(StoreItemManageRequest request) {
        ensureVerifiedOperator(AuthContext.getUserId(), "\u521b\u5efa\u79ef\u5206\u5546\u54c1");
        StoreItem storeItem = new StoreItem();
        applyStoreItemChanges(storeItem, request);
        StoreItem saved = storeItemRepository.save(storeItem);
        adminAuditLogService.log(
                AuthContext.getUserId(),
                "STORE_ITEM_CREATED",
                "STORE_ITEM",
                String.valueOf(saved.getId()),
                "\u521b\u5efa\u79ef\u5206\u5546\u54c1\uff1a" + saved.getName() + "\uff0c\u6240\u9700\u79ef\u5206\uff1a" + saved.getPointsCost() + "\uff0c\u5e93\u5b58\uff1a" + saved.getStock()
        );
        return saved;
    }

    public StoreItem updateStoreItem(Long itemId, StoreItemManageRequest request) {
        ensureVerifiedOperator(AuthContext.getUserId(), "\u66f4\u65b0\u79ef\u5206\u5546\u54c1");
        StoreItem storeItem = storeItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("\u5546\u54c1\u4e0d\u5b58\u5728"));
        applyStoreItemChanges(storeItem, request);
        StoreItem saved = storeItemRepository.save(storeItem);
        adminAuditLogService.log(
                AuthContext.getUserId(),
                "STORE_ITEM_UPDATED",
                "STORE_ITEM",
                String.valueOf(saved.getId()),
                "update store item: " + saved.getName() + ", points=" + saved.getPointsCost() + ", stock=" + saved.getStock() + ", active=" + saved.isActive()
        );
        return saved;
    }

    public RedemptionRecord redeem(Long userId, RedeemRequest request) {
        UserProfile user = getUser(userId);
        StoreItem storeItem = storeItemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("\u5151\u6362\u5546\u54c1\u4e0d\u5b58\u5728"));
        if (!storeItem.isActive()) {
            throw new IllegalArgumentException("\u5f53\u524d\u5546\u54c1\u4e0d\u53ef\u5151\u6362");
        }
        if (storeItem.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("\u5f53\u524d\u5e93\u5b58\u4e0d\u8db3");
        }

        int totalCost = storeItem.getPointsCost() * request.getQuantity();
        if (user.getTotalPoints() < totalCost) {
            throw new IllegalArgumentException("\u5f53\u524d\u79ef\u5206\u4e0d\u8db3");
        }

        RedemptionRecord redemptionRecord = new RedemptionRecord();
        redemptionRecord.setUserId(userId);
        redemptionRecord.setItemId(storeItem.getId());
        redemptionRecord.setItemName(storeItem.getName());
        redemptionRecord.setPointsCost(totalCost);
        redemptionRecord.setQuantity(request.getQuantity());
        redemptionRecord.setDeliveryType(resolveDeliveryType(storeItem));
        redemptionRecord.setRecipientName(normalizeText(request.getRecipientName()));
        redemptionRecord.setRecipientPhone(normalizeText(request.getRecipientPhone()));
        redemptionRecord.setRecipientAddress(normalizeText(request.getRecipientAddress()));
        validateRedemptionRequest(storeItem, redemptionRecord);

        user.setTotalPoints(user.getTotalPoints() - totalCost);
        userProfileRepository.save(user);

        storeItem.setStock(storeItem.getStock() - request.getQuantity());
        storeItemRepository.save(storeItem);

        redemptionRecord.setStatus(RedemptionStatus.CREATED);
        redemptionRecord.setCreatedAt(LocalDateTime.now());
        RedemptionRecord saved = redemptionRecordRepository.save(redemptionRecord);
        notificationService.sendRedemptionCreated(user, saved);
        notificationService.sendRedemptionCreatedOperator(user, saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<RedemptionRecord> listMyRedemptions(Long userId) {
        return redemptionRecordRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<AdminRedemptionView> listAllRedemptions() {
        return redemptionRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(RedemptionRecord::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(RedemptionRecord::getId, Comparator.reverseOrder()))
                .map(record -> {
                    AdminRedemptionView view = new AdminRedemptionView();
                    view.setId(record.getId());
                    view.setUserId(record.getUserId());
                    view.setUserName(resolveDisplayName(getUser(record.getUserId())));
                    view.setItemId(record.getItemId());
                    view.setItemName(record.getItemName());
                    view.setPointsCost(record.getPointsCost());
                    view.setQuantity(record.getQuantity());
                    view.setDeliveryType(record.getDeliveryType());
                    view.setRecipientName(record.getRecipientName());
                    view.setMaskedRecipientPhone(SensitiveMaskingUtils.maskPhoneNo(record.getRecipientPhone()));
                    view.setRecipientAddress(record.getRecipientAddress());
                    view.setDeliveryRemark(record.getDeliveryRemark());
                    view.setStatus(record.getStatus());
                    view.setCreatedAt(record.getCreatedAt());
                    return view;
                })
                .collect(Collectors.toList());
    }

    public RedemptionRecord updateRedemptionStatus(Long redemptionId, RedemptionStatus status, String deliveryRemark) {
        ensureVerifiedOperator(AuthContext.getUserId(), "\u66f4\u65b0\u5151\u6362\u8bb0\u5f55\u72b6\u6001");
        RedemptionRecord record = redemptionRecordRepository.findById(redemptionId)
                .orElseThrow(() -> new IllegalArgumentException("\u5151\u6362\u8bb0\u5f55\u4e0d\u5b58\u5728"));
        if (record.getStatus() != RedemptionStatus.CREATED) {
            throw new IllegalArgumentException("\u5f53\u524d\u5151\u6362\u8bb0\u5f55\u5df2\u5904\u7406\uff0c\u4e0d\u80fd\u91cd\u590d\u53d8\u66f4\u72b6\u6001");
        }
        if (status == RedemptionStatus.CREATED) {
            throw new IllegalArgumentException("\u8bf7\u5c06\u5151\u6362\u8bb0\u5f55\u66f4\u65b0\u4e3a\u5df2\u53d1\u653e\u6216\u5df2\u53d6\u6d88");
        }

        record.setStatus(status);
        record.setDeliveryRemark(normalizeText(deliveryRemark));
        RedemptionRecord saved = redemptionRecordRepository.save(record);
        if (status == RedemptionStatus.CANCELLED) {
            refundRedemption(saved);
        }
        if (status == RedemptionStatus.DELIVERED) {
            notificationService.sendRedemptionDelivered(getUser(saved.getUserId()), saved);
        }
        adminAuditLogService.log(
                AuthContext.getUserId(),
                "REDEMPTION_STATUS_UPDATED",
                "REDEMPTION",
                String.valueOf(saved.getId()),
                "\u66f4\u65b0\u5151\u6362\u8bb0\u5f55\u72b6\u6001\uff0c\u8bb0\u5f55ID\uff1a" + saved.getId() + "\uff0c\u72b6\u6001\uff1a" + saved.getStatus()
        );
        return saved;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardView> listLeaderboard() {
        return userProfileRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.VOLUNTEER || user.getRole() == UserRole.ADMIN)
                .sorted(Comparator.comparing(UserProfile::getTotalPoints).reversed()
                        .thenComparing(UserProfile::getTotalHours).reversed())
                .limit(10)
                .map(user -> {
                    LeaderboardView view = new LeaderboardView();
                    view.setUserId(user.getId());
                    view.setUserName(user.getName());
                    view.setBadgeName(user.getBadgeName());
                    view.setTotalHours(user.getTotalHours());
                    view.setTotalPoints(user.getTotalPoints());
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HeatmapPointView> buildHeatmap() {
        return volunteerActivityRepository.findAll().stream()
                .map(activity -> {
                    HeatmapPointView view = new HeatmapPointView();
                    view.setActivityId(activity.getId());
                    view.setActivityTitle(activity.getTitle());
                    view.setLocation(activity.getLocation());
                    view.setLatitude(activity.getLatitude());
                    view.setLongitude(activity.getLongitude());
                    view.setIntensity(activity.getEnrolledCount());
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecommendedActivityView> recommendActivities(Long userId) {
        List<Long> enrolledIds = participationRecordRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(ParticipationRecord::getActivityId)
                .distinct()
                .collect(Collectors.toList());

        return volunteerActivityRepository.findAll().stream()
                .filter(activity -> !enrolledIds.contains(activity.getId()))
                .filter(activity -> activity.getStartTime() != null && activity.getStartTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(VolunteerActivity::getEnrolledCount).reversed()
                        .thenComparing(VolunteerActivity::getStartTime))
                .limit(5)
                .map(activity -> {
                    RecommendedActivityView view = new RecommendedActivityView();
                    view.setActivityId(activity.getId());
                    view.setTitle(activity.getTitle());
                    view.setLocation(activity.getLocation());
                    view.setEnrolledCount(activity.getEnrolledCount());
                    view.setRecommendationReason(activity.getEnrolledCount() > 0 ? "\u6d3b\u52a8\u5df2\u6709\u62a5\u540d\u8bb0\u5f55\uff0c\u8bf4\u660e\u9700\u6c42\u660e\u786e\uff0c\u9002\u5408\u4f18\u5148\u63a8\u8350" : "\u6d3b\u52a8\u5f53\u524d\u62a5\u540d\u8f83\u5c11\uff0c\u9002\u5408\u5f15\u5bfc\u66f4\u591a\u5fd7\u613f\u8005\u53c2\u4e0e");
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HeatmapPointView> buildSmartHeatmap() {
        return volunteerActivityRepository.findAll().stream()
                .map(activity -> {
                    int gapCount = Math.max(0, activity.getCapacity() - activity.getEnrolledCount());
                    int demandLevel = resolveDemandLevel(activity.getDemandLevel());
                    int intensity = demandLevel * 10 + gapCount * 3 + Math.min(activity.getEnrolledCount(), 10);

                    HeatmapPointView view = new HeatmapPointView();
                    view.setActivityId(activity.getId());
                    view.setActivityTitle(activity.getTitle());
                    view.setLocation(activity.getLocation());
                    view.setCategory(normalizeActivityCategory(activity.getCategory()));
                    view.setLatitude(activity.getLatitude());
                    view.setLongitude(activity.getLongitude());
                    view.setIntensity(intensity);
                    view.setDemandLevel(demandLevel);
                    view.setGapCount(gapCount);
                    view.setAreaTag(resolveAreaTag(activity, gapCount, demandLevel));
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecommendedActivityView> recommendActivitiesV2(Long userId) {
        UserProfile user = getUser(userId);
        List<ParticipationRecord> myRecords = participationRecordRepository.findByUserIdOrderByIdDesc(userId);
        List<ParticipationRecord> allRecords = participationRecordRepository.findAll();
        List<VolunteerActivity> allActivities = volunteerActivityRepository.findAll();
        Map<Long, VolunteerActivity> activityMap = allActivities.stream()
                .collect(Collectors.toMap(VolunteerActivity::getId, activity -> activity));
        Map<Long, ServiceSite> siteMap = serviceSiteRepository.findAll().stream()
                .collect(Collectors.toMap(ServiceSite::getId, site -> site));
        Set<Long> enrolledIds = myRecords.stream()
                .map(ParticipationRecord::getActivityId)
                .filter(activityMap::containsKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<ParticipationRecord> completedRecords = myRecords.stream()
                .filter(record -> record.getStatus() == ParticipationStatus.COMPLETED)
                .filter(record -> activityMap.containsKey(record.getActivityId()))
                .collect(Collectors.toList());
        List<VolunteerActivity> historyActivities = completedRecords.stream()
                .map(record -> activityMap.get(record.getActivityId()))
                .filter(activity -> activity != null)
                .collect(Collectors.toList());
        RecommendationProfile profile = buildRecommendationProfile(user, completedRecords, historyActivities, siteMap);
        List<VolunteerActivity> candidates = allActivities.stream()
                .filter(activity -> activity.getStatus() == ActivityStatus.PUBLISHED)
                .filter(activity -> activity.getStartTime() != null && activity.getStartTime().isAfter(LocalDateTime.now()))
                .filter(activity -> activity.getCapacity() > activity.getEnrolledCount())
                .filter(activity -> !enrolledIds.contains(activity.getId()))
                .collect(Collectors.toList());
        if (candidates.isEmpty()) {
            return new ArrayList<>();
        }

        List<RecommendationCandidate> ranked = candidates.stream()
                .map(activity -> buildRecommendationCandidate(user, activity, myRecords, allRecords, profile, activityMap, siteMap))
                .sorted(Comparator.comparingInt(RecommendationCandidate::getFinalScore).reversed()
                        .thenComparing(RecommendationCandidate::getBusinessScore).reversed()
                        .thenComparing(candidate -> candidate.getActivity().getStartTime()))
                .collect(Collectors.toList());

        List<RecommendationCandidate> diversified = diversifyRecommendations(ranked, profile);
        return diversified.stream()
                .limit(RECOMMENDATION_LIMIT)
                .map(this::buildRecommendedActivityView)
                .collect(Collectors.toList());
    }

    private RecommendationProfile buildRecommendationProfile(UserProfile user,
                                                             List<ParticipationRecord> completedRecords,
                                                             List<VolunteerActivity> historyActivities,
                                                             Map<Long, ServiceSite> siteMap) {
        RecommendationProfile profile = new RecommendationProfile();
        profile.coldStart = completedRecords.size() < 2;

        Map<String, Integer> categoryFrequency = new HashMap<>();
        Map<Integer, Integer> difficultyFrequency = new HashMap<>();
        Map<String, Integer> communityFrequency = new HashMap<>();
        Map<Integer, Integer> hourFrequency = new HashMap<>();

        for (int i = 0; i < completedRecords.size(); i++) {
            ParticipationRecord record = completedRecords.get(i);
            VolunteerActivity activity = historyActivities.get(i);
            String category = normalizeActivityCategory(activity.getCategory());
            incrementCount(categoryFrequency, category, resolveImplicitWeight(record));

            int difficultyLevel = resolveDifficultyLevel(activity.getDifficultyLevel());
            incrementCount(difficultyFrequency, difficultyLevel, resolveImplicitWeight(record));

            ServiceSite site = siteMap.get(activity.getServiceSiteId());
            String communityKey = resolveCommunityKey(activity, site);
            incrementCount(communityFrequency, communityKey, resolveImplicitWeight(record));

            LocalDateTime anchorTime = record.getCheckInTime() != null ? record.getCheckInTime() : activity.getStartTime();
            if (anchorTime != null) {
                incrementCount(hourFrequency, anchorTime.getHour(), resolveImplicitWeight(record));
            }
        }

        profile.preferredCategories = topKeys(categoryFrequency, 3);
        profile.preferredDifficulty = topIntegerKeys(difficultyFrequency, 2);
        profile.preferredCommunities = topKeys(communityFrequency, 3);
        profile.preferredHours = topIntegerKeys(hourFrequency, 3);
        return profile;
    }

    private RecommendationCandidate buildRecommendationCandidate(UserProfile user,
                                                                 VolunteerActivity activity,
                                                                 List<ParticipationRecord> myRecords,
                                                                 List<ParticipationRecord> allRecords,
                                                                 RecommendationProfile profile,
                                                                 Map<Long, VolunteerActivity> activityMap,
                                                                 Map<Long, ServiceSite> siteMap) {
        int contentScore = calculateContentScore(user, activity, profile, siteMap);
        int collaborativeScore = calculateCollaborativeScore(activity, myRecords, allRecords, activityMap);
        int businessScore = calculateBusinessRecommendationScore(activity);
        int qualityScore = calculateQualityScore(activity, allRecords);
        int diversityScore = calculateDiversitySeedScore(activity, profile);

        int finalScore = clampScore(
                contentScore * 0.40
                        + collaborativeScore * 0.25
                        + businessScore * 0.20
                        + qualityScore * 0.10
                        + diversityScore * 0.05
        );

        RecommendationCandidate candidate = new RecommendationCandidate();
        candidate.activity = activity;
        candidate.contentScore = contentScore;
        candidate.collaborativeScore = collaborativeScore;
        candidate.businessScore = businessScore;
        candidate.qualityScore = qualityScore;
        candidate.diversityScore = diversityScore;
        candidate.finalScore = finalScore;
        candidate.reason = buildRecommendationReason(user, activity, profile, collaborativeScore, businessScore, qualityScore, siteMap);
        return candidate;
    }

    private int calculateContentScore(UserProfile user,
                                      VolunteerActivity activity,
                                      RecommendationProfile profile,
                                      Map<Long, ServiceSite> siteMap) {
        if (profile.coldStart) {
            int difficultyScore = resolveDifficultyMatchScore(user, resolveDifficultyLevel(activity.getDifficultyLevel())) * 10;
            int demandScore = resolveDemandLevel(activity.getDemandLevel()) * 16;
            int gapScore = normalizeGapScore(activity);
            int popularityScore = normalizePopularityScore(activity);
            return clampScore(difficultyScore * 0.40 + demandScore * 0.30 + gapScore * 0.20 + popularityScore * 0.10);
        }

        int categoryScore = profile.preferredCategories.contains(normalizeActivityCategory(activity.getCategory())) ? 100 : 35;
        int difficultyScore = profile.preferredDifficulty.contains(resolveDifficultyLevel(activity.getDifficultyLevel()))
                ? 100
                : resolveDifficultyMatchScore(user, resolveDifficultyLevel(activity.getDifficultyLevel())) * 10;
        ServiceSite site = siteMap.get(activity.getServiceSiteId());
        String communityKey = resolveCommunityKey(activity, site);
        int communityScore = profile.preferredCommunities.contains(communityKey) ? 100 : 40;
        int timeScore = calculateTimePreferenceScore(activity, profile);

        return clampScore(categoryScore * 0.35 + difficultyScore * 0.25 + communityScore * 0.20 + timeScore * 0.20);
    }

    private int calculateCollaborativeScore(VolunteerActivity candidate,
                                            List<ParticipationRecord> myRecords,
                                            List<ParticipationRecord> allRecords,
                                            Map<Long, VolunteerActivity> activityMap) {
        Map<Long, Set<Long>> activityUsersMap = allRecords.stream()
                .filter(record -> record.getStatus() == ParticipationStatus.APPROVED
                        || record.getStatus() == ParticipationStatus.CHECKED_IN
                        || record.getStatus() == ParticipationStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        ParticipationRecord::getActivityId,
                        Collectors.mapping(ParticipationRecord::getUserId, Collectors.toSet())
                ));

        Set<Long> candidateUsers = activityUsersMap.get(candidate.getId());
        if (candidateUsers == null || candidateUsers.isEmpty()) {
            return 0;
        }

        double weightedSimilarity = 0;
        double totalWeight = 0;
        for (ParticipationRecord record : myRecords) {
            VolunteerActivity historyActivity = activityMap.get(record.getActivityId());
            if (historyActivity == null || record.getStatus() == ParticipationStatus.PENDING) {
                continue;
            }
            Set<Long> historyUsers = activityUsersMap.get(historyActivity.getId());
            if (historyUsers == null || historyUsers.isEmpty()) {
                continue;
            }
            double similarity = jaccardSimilarity(historyUsers, candidateUsers);
            if (similarity <= 0) {
                continue;
            }
            int weight = resolveImplicitWeight(record);
            weightedSimilarity += similarity * weight;
            totalWeight += weight;
        }

        if (totalWeight <= 0) {
            return 0;
        }
        return clampScore((weightedSimilarity / totalWeight) * 100);
    }

    private int calculateBusinessRecommendationScore(VolunteerActivity activity) {
        int demandScore = resolveDemandLevel(activity.getDemandLevel()) * 20;
        int gapScore = normalizeGapScore(activity);
        return clampScore(demandScore * 0.55 + gapScore * 0.45);
    }

    private int calculateQualityScore(VolunteerActivity activity, List<ParticipationRecord> allRecords) {
        List<ParticipationRecord> activityRecords = allRecords.stream()
                .filter(record -> activity.getId().equals(record.getActivityId()))
                .collect(Collectors.toList());
        if (activityRecords.isEmpty()) {
            return 55;
        }

        double averageRating = activityRecords.stream()
                .filter(record -> record.getServiceRating() != null)
                .mapToInt(ParticipationRecord::getServiceRating)
                .average()
                .orElse(4.0);
        double ratingScore = averageRating / 5.0 * 100;
        long anomalyCount = activityRecords.stream()
                .filter(record -> record.getAnomalyStatus() == AnomalyStatus.CONFIRMED)
                .count();
        double anomalyPenalty = Math.min(35, anomalyCount * 12);
        return clampScore(ratingScore - anomalyPenalty + 10);
    }

    private int calculateDiversitySeedScore(VolunteerActivity activity, RecommendationProfile profile) {
        if (profile.preferredCategories.isEmpty()) {
            return 50;
        }
        return profile.preferredCategories.contains(normalizeActivityCategory(activity.getCategory())) ? 35 : 85;
    }

    private int calculateTimePreferenceScore(VolunteerActivity activity, RecommendationProfile profile) {
        if (activity.getStartTime() == null || profile.preferredHours.isEmpty()) {
            return 50;
        }
        int hour = activity.getStartTime().getHour();
        int minGap = profile.preferredHours.stream()
                .mapToInt(preferredHour -> Math.abs(preferredHour - hour))
                .min()
                .orElse(6);
        if (minGap <= 1) {
            return 100;
        }
        if (minGap <= 3) {
            return 75;
        }
        return 45;
    }

    private int normalizeGapScore(VolunteerActivity activity) {
        if (activity.getCapacity() <= 0) {
            return 0;
        }
        double gapRatio = Math.max(0, activity.getCapacity() - activity.getEnrolledCount()) * 1.0 / activity.getCapacity();
        return clampScore(gapRatio * 100);
    }

    private int normalizePopularityScore(VolunteerActivity activity) {
        if (activity.getCapacity() <= 0) {
            return 0;
        }
        double ratio = Math.min(1.0, activity.getEnrolledCount() * 1.0 / activity.getCapacity());
        return clampScore(ratio * 100);
    }

    private List<RecommendationCandidate> diversifyRecommendations(List<RecommendationCandidate> ranked,
                                                                   RecommendationProfile profile) {
        if (ranked.size() <= RECOMMENDATION_LIMIT) {
            return ranked;
        }

        List<RecommendationCandidate> selected = new ArrayList<>();
        Set<String> usedCategories = new HashSet<>();
        for (RecommendationCandidate candidate : ranked) {
            String category = normalizeActivityCategory(candidate.getActivity().getCategory());
            boolean preferredCategory = profile.preferredCategories.contains(category);
            if (selected.size() < RECOMMENDATION_LIMIT - 1 || preferredCategory || usedCategories.contains(category)) {
                selected.add(candidate);
                usedCategories.add(category);
            } else {
                selected.add(candidate);
                usedCategories.add(category);
                break;
            }
            if (selected.size() == RECOMMENDATION_LIMIT) {
                break;
            }
        }

        if (selected.size() < RECOMMENDATION_LIMIT) {
            for (RecommendationCandidate candidate : ranked) {
                if (!selected.contains(candidate)) {
                    selected.add(candidate);
                }
                if (selected.size() == RECOMMENDATION_LIMIT) {
                    break;
                }
            }
        }
        return selected;
    }

    private RecommendedActivityView buildRecommendedActivityView(RecommendationCandidate candidate) {
        VolunteerActivity activity = candidate.getActivity();
        RecommendedActivityView view = new RecommendedActivityView();
        view.setActivityId(activity.getId());
        view.setTitle(activity.getTitle());
        view.setLocation(activity.getLocation());
        view.setCategory(normalizeActivityCategory(activity.getCategory()));
        view.setDifficultyLevel(resolveDifficultyLevel(activity.getDifficultyLevel()));
        view.setEnrolledCount(activity.getEnrolledCount());
        view.setRecommendationScore(candidate.getFinalScore());
        view.setRecommendationReason(candidate.getReason());
        return view;
    }

    private String buildRecommendationReason(UserProfile user,
                                             VolunteerActivity activity,
                                             RecommendationProfile profile,
                                             int collaborativeScore,
                                             int businessScore,
                                             int qualityScore,
                                             Map<Long, ServiceSite> siteMap) {
        String category = normalizeActivityCategory(activity.getCategory());
        if (profile.preferredCategories.contains(category)) {
            return "匹配你的常参与类别：" + category;
        }
        ServiceSite site = siteMap.get(activity.getServiceSiteId());
        if (profile.preferredCommunities.contains(resolveCommunityKey(activity, site))) {
            return "匹配你常参与的服务社区";
        }
        if (collaborativeScore >= 60) {
            return "与你历史参与活动的相似用户偏好一致";
        }
        if (businessScore >= 75) {
            return "当前需求高且仍有服务缺口";
        }
        if (qualityScore >= 80 && user.getTotalHours() >= 15) {
            return "活动质量稳定，适合继续参与";
        }
        if (profile.coldStart) {
            return "结合当前经验阶段与活动难度推荐";
        }
        return "结合类别、难度与服务需求综合推荐";
    }

    private double jaccardSimilarity(Set<Long> left, Set<Long> right) {
        if (left == null || right == null || left.isEmpty() || right.isEmpty()) {
            return 0;
        }
        Set<Long> intersection = new HashSet<Long>(left);
        intersection.retainAll(right);
        if (intersection.isEmpty()) {
            return 0;
        }
        Set<Long> union = new HashSet<Long>(left);
        union.addAll(right);
        return union.isEmpty() ? 0 : intersection.size() * 1.0 / union.size();
    }

    private int resolveImplicitWeight(ParticipationRecord record) {
        if (record == null || record.getStatus() == null) {
            return 1;
        }
        if (record.getStatus() == ParticipationStatus.COMPLETED) {
            int ratingWeight = record.getServiceRating() == null ? 2 : Math.max(2, record.getServiceRating());
            int hourWeight = record.getServiceHours() >= 6 ? 2 : 1;
            return ratingWeight + hourWeight;
        }
        if (record.getStatus() == ParticipationStatus.CHECKED_IN) {
            return 3;
        }
        if (record.getStatus() == ParticipationStatus.APPROVED) {
            return 2;
        }
        return 1;
    }

    private <T> void incrementCount(Map<T, Integer> counter, T key, int weight) {
        if (key == null) {
            return;
        }
        counter.put(key, counter.getOrDefault(key, 0) + Math.max(1, weight));
    }

    private List<String> topKeys(Map<String, Integer> counter, int limit) {
        return counter.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private List<Integer> topIntegerKeys(Map<Integer, Integer> counter, int limit) {
        return counter.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static class RecommendationProfile {
        private boolean coldStart;
        private List<String> preferredCategories = new ArrayList<String>();
        private List<Integer> preferredDifficulty = new ArrayList<Integer>();
        private List<String> preferredCommunities = new ArrayList<String>();
        private List<Integer> preferredHours = new ArrayList<Integer>();
    }

    private static class RecommendationCandidate {
        private VolunteerActivity activity;
        private int contentScore;
        private int collaborativeScore;
        private int businessScore;
        private int qualityScore;
        private int diversityScore;
        private int finalScore;
        private String reason;

        public VolunteerActivity getActivity() {
            return activity;
        }

        public int getBusinessScore() {
            return businessScore;
        }

        public int getFinalScore() {
            return finalScore;
        }

        public String getReason() {
            return reason;
        }
    }

    private void validateCheckIn(VolunteerActivity activity, CheckInRequest request) {
        if (request.getMethod() == CheckInMethod.QR_CODE) {
            String expectedCode = checkInCodeCacheService.getCode(activity.getId()).orElse(activity.getCheckInCode());
            if (request.getCheckInCode() == null || !request.getCheckInCode().equalsIgnoreCase(expectedCode)) {
                throw new IllegalArgumentException("\u52a8\u6001\u7b7e\u5230\u7801\u9519\u8bef");
            }
            return;
        }

        if (request.getMethod() == CheckInMethod.GEOFENCE) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new IllegalArgumentException("\u56f4\u680f\u7b7e\u5230\u9700\u8981\u4e0a\u4f20\u7ecf\u7eac\u5ea6");
            }
            if (!isWithinGeofence(activity, request.getLatitude(), request.getLongitude())) {
                throw new IllegalArgumentException("\u5f53\u524d\u4f4d\u7f6e\u4e0d\u5728\u670d\u52a1\u8303\u56f4\u5185");
            }
            return;
        }

        if (request.getMethod() == CheckInMethod.MANUAL) {
            throw new IllegalArgumentException("\u624b\u52a8\u8865\u7b7e\u8bf7\u8d70\u8865\u7b7e\u7533\u8bf7\u6d41\u7a0b");
        }
    }

    private ParticipationRecord completeRecord(ParticipationRecord record,
                                               VolunteerActivity activity,
                                               UserProfile user,
                                               LocalDateTime checkOutTime,
                                               Double latitude,
                                               Double longitude) {
        record.setCheckOutTime(checkOutTime);
        record.setLastLatitude(maskCoordinate(latitude));
        record.setLastLongitude(maskCoordinate(longitude));

        double serviceHours = calculateHours(record.getCheckInTime(), checkOutTime);
        record.setServiceHours(serviceHours);
        record.setEarnedPoints(0);
        record.setServiceRating(null);
        record.setServiceComment(null);
        record.setPointBreakdown("\u5f85\u7ba1\u7406\u5458\u8bc4\u4ef7\u540e\u7ed3\u7b97\u79ef\u5206");
        record.setRewardFinalizedAt(null);
        record.setCertificateNo(null);
        record.setCertificateUrl(null);
        record.setEvidenceHash(null);
        record.setStatus(ParticipationStatus.COMPLETED);
        ParticipationRecord savedRecord = participationRecordRepository.save(record);

        saveTrackPoint(activity.getId(), user.getId(), latitude, longitude, checkOutTime, TrackPointType.END, true);
        List<TrackPoint> trackPoints = trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(activity.getId(), user.getId());
        saveBlockchainProof(
                savedRecord,
                activity,
                user,
                BlockchainEventType.CHECK_OUT,
                buildGenericHash("CHECK_OUT|" + savedRecord.getId() + "|" + checkOutTime + "|" + serviceHours
                        + "|" + maskCoordinate(latitude) + "|" + maskCoordinate(longitude)),
                "\u7b7e\u9000\u5b58\u8bc1\uff0c\u670d\u52a1\u65f6\u957f\uff1a" + serviceHours + " \u5c0f\u65f6\uff0c\u5750\u6807\uff1a" + maskCoordinate(latitude) + ", " + maskCoordinate(longitude),
                checkOutTime
        );
        saveBlockchainProof(
                savedRecord,
                activity,
                user,
                BlockchainEventType.TRACK_SUMMARY,
                buildGenericHash("TRACK_SUMMARY|" + savedRecord.getId() + "|" + summarizeTrackPoints(trackPoints)),
                summarizeTrackPoints(trackPoints),
                checkOutTime
        );

        user.setTotalHours(round(user.getTotalHours() + serviceHours));
        user.setBadgeName(resolveBadgeName(user.getTotalHours()));
        userProfileRepository.save(user);
        notificationService.sendTaskCompleted(user, activity, savedRecord.getId(), serviceHours);
        notificationService.sendServiceConfirmPending(activity, user, savedRecord.getId(), serviceHours);
        return savedRecord;
    }

    private ParticipationRecord findRecord(Long activityId, Long userId) {
        return participationRecordRepository.findByActivityIdAndUserId(activityId, userId)
                .orElseThrow(() -> new IllegalArgumentException("\u8bf7\u5148\u62a5\u540d\u6d3b\u52a8"));
    }

    private VolunteerActivity getManageableActivity(Long activityId, Long operatorId) {
        VolunteerActivity activity = getActivity(activityId);
        UserProfile operator = getUser(operatorId);
        if (!canManageActivity(activity, operator)) {
            throw new IllegalArgumentException("\u65e0\u6743\u64cd\u4f5c\u8be5\u6d3b\u52a8");
        }
        return activity;
    }

    private boolean canManageActivity(Long activityId, UserProfile operator) {
        VolunteerActivity activity = getActivity(activityId);
        return canManageActivity(activity, operator);
    }

    private boolean canManageActivity(VolunteerActivity activity, UserProfile operator) {
        if (activity == null || operator == null || operator.getRole() == null) {
            return false;
        }
        if (operator.getRole() == UserRole.SUPER_ADMIN) {
            return true;
        }
        return operator.getRole() == UserRole.ADMIN
                && activity.getOrganizerId() != null
                && activity.getOrganizerId().equals(operator.getId());
    }

    private String normalizeSnapshotImageUrl(String imageUrl) {
        String normalized = imageUrl == null ? "" : imageUrl.trim();
        if (!StringUtils.hasText(normalized)) {
            throw new IllegalArgumentException("\u5feb\u7167\u56fe\u7247\u5730\u5740\u4e0d\u80fd\u4e3a\u7a7a");
        }
        if (!normalized.startsWith("/files/")) {
            throw new IllegalArgumentException("\u5feb\u7167\u56fe\u7247\u5730\u5740\u65e0\u6548");
        }
        if (!fileStorageService.exists(normalized)) {
            throw new IllegalArgumentException("\u5feb\u7167\u56fe\u7247\u4e0d\u5b58\u5728");
        }
        return normalized;
    }

    private UserProfile getUser(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("\u7528\u6237\u4e0d\u5b58\u5728"));
    }

    private UserProfile ensureVerifiedOperator(Long userId, String actionName) {
        UserProfile operator = getUser(userId);
        identityVerificationService.ensureVerified(operator, actionName);
        return operator;
    }

    private String resolveCertificateVolunteerName(UserProfile user) {
        if (user == null) {
            return "\u5fd7\u613f\u8005";
        }
        if (StringUtils.hasText(user.getRealName())) {
            return user.getRealName().trim();
        }
        return resolveDisplayName(user);
    }

    private String maskIdCardNo(String value) {
        return SensitiveMaskingUtils.maskIdCardNo(value);
    }

    private String generateCode() {
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    private void applyStoreItemChanges(StoreItem storeItem, StoreItemManageRequest request) {
        storeItem.setName(request.getName());
        storeItem.setCategory(request.getCategory());
        storeItem.setDescription(request.getDescription());
        storeItem.setPointsCost(request.getPointsCost());
        storeItem.setStock(request.getStock());
        storeItem.setActive(request.isActive());
        storeItem.setDeliveryType(request.getDeliveryType() == null ? StoreItemDeliveryType.VIRTUAL : request.getDeliveryType());
    }

    private void validateRedemptionRequest(StoreItem storeItem, RedemptionRecord record) {
        if (resolveDeliveryType(storeItem) != StoreItemDeliveryType.PHYSICAL) {
            return;
        }
        if (!StringUtils.hasText(record.getRecipientName())
                || !StringUtils.hasText(record.getRecipientPhone())
                || !StringUtils.hasText(record.getRecipientAddress())) {
            throw new IllegalArgumentException("实体商品请先填写收货人、手机号和收货地址");
        }
    }

    private StoreItemDeliveryType resolveDeliveryType(StoreItem storeItem) {
        if (storeItem == null || storeItem.getDeliveryType() == null) {
            return StoreItemDeliveryType.VIRTUAL;
        }
        return storeItem.getDeliveryType();
    }

    private String normalizeText(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private void refundRedemption(RedemptionRecord record) {
        UserProfile user = getUser(record.getUserId());
        user.setTotalPoints(user.getTotalPoints() + record.getPointsCost());
        userProfileRepository.save(user);

        storeItemRepository.findById(record.getItemId()).ifPresent(storeItem -> {
            storeItem.setStock(storeItem.getStock() + record.getQuantity());
            storeItemRepository.save(storeItem);
        });
    }

    private List<VolunteerActivity> listManagedActivities(Long adminUserId, Integer year) {
        return volunteerActivityRepository.findAll().stream()
                .filter(activity -> adminUserId != null && adminUserId.equals(activity.getOrganizerId()))
                .filter(activity -> year == null || isInYear(activity.getStartTime(), year))
                .collect(Collectors.toList());
    }

    private List<UserProfile> resolveParticipantUsers() {
        return userProfileRepository.findAll().stream()
                .filter(user -> user != null)
                .filter(user -> user.getRole() == UserRole.VOLUNTEER || user.getRole() == UserRole.ADMIN)
                .collect(Collectors.toList());
    }

    private List<StarLeaderboardView> buildStarLeaderboard(List<UserProfile> users,
                                                           List<VolunteerActivity> scopedActivities,
                                                           String period) {
        Set<Long> scopedActivityIds = scopedActivities.stream()
                .map(VolunteerActivity::getId)
                .collect(Collectors.toSet());
        if (scopedActivityIds.isEmpty()) {
            return new ArrayList<>();
        }

        LocalDateTime startAt = resolvePeriodStart(period);
        LocalDateTime endAt = LocalDateTime.now();
        List<ParticipationRecord> scopedRecords = participationRecordRepository.findAll().stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> scopedActivityIds.contains(record.getActivityId()))
                .filter(record -> isInPeriod(resolveRecordTime(record), startAt, endAt))
                .collect(Collectors.toList());

        if (scopedRecords.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, VolunteerActivity> activityMap = volunteerActivityRepository.findAllById(scopedActivityIds).stream()
                .collect(Collectors.toMap(VolunteerActivity::getId, activity -> activity));
        Map<Long, UserProfile> userMap = users.stream()
                .collect(Collectors.toMap(UserProfile::getId, user -> user));

        return scopedRecords.stream()
                .collect(Collectors.groupingBy(ParticipationRecord::getUserId))
                .entrySet()
                .stream()
                .map(entry -> buildStarLeaderboardView(
                        userMap.get(entry.getKey()),
                        entry.getValue(),
                        activityMap,
                        normalizeAnalyticsPeriod(period)
                ))
                .filter(item -> item != null)
                .sorted(Comparator.comparing(StarLeaderboardView::getStarScore).reversed()
                        .thenComparing(StarLeaderboardView::getPositiveRatingRate).reversed()
                        .thenComparing(StarLeaderboardView::getTotalHours).reversed()
                        .thenComparing(StarLeaderboardView::getCompletionCount).reversed()
                        .thenComparing(StarLeaderboardView::getUserId))
                .limit(10)
                .collect(Collectors.toList());
    }

    private StarLeaderboardView buildStarLeaderboardView(UserProfile user,
                                                         List<ParticipationRecord> records,
                                                         Map<Long, VolunteerActivity> activityMap,
                                                         String period) {
        if (user == null || records == null || records.isEmpty()) {
            return null;
        }

        double totalHours = round(records.stream().mapToDouble(ParticipationRecord::getServiceHours).sum());
        int totalPoints = records.stream().mapToInt(ParticipationRecord::getEarnedPoints).sum();
        int completionCount = records.size();
        long ratedCount = records.stream().filter(record -> record.getServiceRating() != null).count();
        long positiveCount = records.stream()
                .filter(record -> record.getServiceRating() != null && record.getServiceRating() >= 4)
                .count();
        double positiveRatingRate = ratedCount == 0 ? 0 : round((positiveCount * 100D) / ratedCount);

        double trackCoverageScore = round(records.stream()
                .mapToDouble(record -> calculateTrackCoverageScore(record, activityMap.get(record.getActivityId())))
                .average()
                .orElse(0));
        double starScore = round(totalHours * 4 + positiveRatingRate * 0.35 + completionCount * 9 + trackCoverageScore * 0.2);

        Map<String, Long> areaMap = records.stream()
                .map(record -> activityMap.get(record.getActivityId()))
                .filter(activity -> activity != null)
                .map(activity -> resolveAreaLabel(activity.getLocation()))
                .filter(StringUtils::hasText)
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        String topArea = areaMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("未标注社区");

        ParticipationRecord highlightRecord = records.stream()
                .sorted(Comparator.comparing(ParticipationRecord::getServiceRating, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ParticipationRecord::getServiceHours, Comparator.reverseOrder())
                        .thenComparing(this::resolveRecordTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .findFirst()
                .orElse(records.get(0));
        VolunteerActivity highlightActivity = activityMap.get(highlightRecord.getActivityId());

        StarLeaderboardView view = new StarLeaderboardView();
        view.setUserId(user.getId());
        view.setUserName(resolveDisplayName(user));
        view.setAvatarUrl(user.getAvatarUrl());
        view.setBadgeName(user.getBadgeName());
        view.setPeriod(period);
        view.setTotalHours(totalHours);
        view.setTotalPoints(totalPoints);
        view.setCompletionCount(completionCount);
        view.setPositiveRatingRate(positiveRatingRate);
        view.setTrackCoverageScore(trackCoverageScore);
        view.setStarScore(starScore);
        view.setTopArea(topArea);
        view.setHighlightTitle(highlightActivity == null ? "持续服务中" : highlightActivity.getTitle());
        view.setShowcaseText(buildShowcaseText(view, highlightActivity));
        return view;
    }

    private String buildShowcaseText(StarLeaderboardView view, VolunteerActivity highlightActivity) {
        String activityTitle = highlightActivity == null ? "近期服务活动" : highlightActivity.getTitle();
        String category = highlightActivity == null ? "综合服务" : normalizeActivityCategory(highlightActivity.getCategory());
        return "在" + view.getTopArea()
                + "持续活跃，"
                + view.getCompletionCount()
                + "次服务覆盖"
                + category
                + "，代表活动："
                + activityTitle
                + "。";
    }

    private double calculateTrackCoverageScore(ParticipationRecord record, VolunteerActivity activity) {
        if (record == null || activity == null) {
            return 0;
        }
        List<TrackPoint> trackPoints = trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(
                record.getActivityId(),
                record.getUserId()
        );
        int trackCount = trackPoints.size();
        int snapshotCount = serviceSnapshotRepository
                .findByActivityIdAndUserIdOrderByCreatedAtDesc(record.getActivityId(), record.getUserId())
                .size();
        int baseScore = Math.min(100, trackCount * 18 + snapshotCount * 16);
        if (record.getCheckInTime() != null) {
            baseScore += 8;
        }
        if (record.getCheckOutTime() != null) {
            baseScore += 8;
        }
        return Math.min(100, baseScore);
    }

    private List<CommunityInsightView> buildCommunityInsights(List<VolunteerActivity> scopedActivities, String period) {
        if (scopedActivities == null || scopedActivities.isEmpty()) {
            return new ArrayList<>();
        }
        LocalDateTime startAt = resolvePeriodStart(period);
        LocalDateTime endAt = LocalDateTime.now();
        List<VolunteerActivity> activities = scopedActivities.stream()
                .filter(activity -> isInPeriod(activity.getStartTime(), startAt, endAt))
                .collect(Collectors.toList());
        if (activities.isEmpty()) {
            return new ArrayList<>();
        }

        Map<Long, ServiceSite> siteMap = serviceSiteRepository.findAll().stream()
                .collect(Collectors.toMap(ServiceSite::getId, site -> site));
        Set<Long> scopedActivityIds = activities.stream().map(VolunteerActivity::getId).collect(Collectors.toSet());
        List<ParticipationRecord> scopedRecords = participationRecordRepository.findAll().stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> scopedActivityIds.contains(record.getActivityId()))
                .filter(record -> isInPeriod(resolveRecordTime(record), startAt, endAt))
                .collect(Collectors.toList());

        Map<Long, List<ParticipationRecord>> recordsByActivity = scopedRecords.stream()
                .collect(Collectors.groupingBy(ParticipationRecord::getActivityId));
        Map<Long, List<TrackPoint>> trackPointMap = new HashMap<>();
        for (ParticipationRecord record : scopedRecords) {
            String key = record.getActivityId() + "_" + record.getUserId();
            if (trackPointMap.containsKey(record.getId())) {
                continue;
            }
            trackPointMap.put(record.getId(), trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(
                    record.getActivityId(),
                    record.getUserId()
            ));
        }

        return activities.stream()
                .collect(Collectors.groupingBy(activity -> resolveCommunityKey(activity, siteMap.get(activity.getServiceSiteId()))))
                .entrySet()
                .stream()
                .map(entry -> buildCommunityInsightView(entry.getValue(), siteMap, recordsByActivity, trackPointMap))
                .filter(item -> item != null)
                .sorted(Comparator.comparing(CommunityInsightView::getDemandIndex).reversed()
                        .thenComparing(CommunityInsightView::getVitalityIndex).reversed()
                        .thenComparing(CommunityInsightView::getCommunityName))
                .collect(Collectors.toList());
    }

    private CommunityInsightView buildCommunityInsightView(List<VolunteerActivity> activities,
                                                           Map<Long, ServiceSite> siteMap,
                                                           Map<Long, List<ParticipationRecord>> recordsByActivity,
                                                           Map<Long, List<TrackPoint>> trackPointMap) {
        if (activities == null || activities.isEmpty()) {
            return null;
        }

        VolunteerActivity anchor = activities.get(0);
        ServiceSite site = siteMap.get(anchor.getServiceSiteId());
        int gapCount = activities.stream()
                .mapToInt(activity -> Math.max(0, activity.getCapacity() - activity.getEnrolledCount()))
                .sum();
        int totalEnrollments = activities.stream()
                .mapToInt(VolunteerActivity::getEnrolledCount)
                .sum();
        int demandLevel = activities.stream()
                .map(VolunteerActivity::getDemandLevel)
                .filter(item -> item != null)
                .mapToInt(this::resolveDemandLevel)
                .max()
                .orElse(3);

        Set<Long> uniqueUsers = new HashSet<>();
        int totalTrackPoints = 0;
        double totalHours = 0;
        int completedCount = 0;
        int ratedCount = 0;
        int positiveRatingCount = 0;
        int activitiesWithTracks = 0;
        for (VolunteerActivity activity : activities) {
            List<ParticipationRecord> records = recordsByActivity.getOrDefault(activity.getId(), new ArrayList<>());
            int activityTrackPoints = 0;
            for (ParticipationRecord record : records) {
                uniqueUsers.add(record.getUserId());
                totalHours += record.getServiceHours();
                if (record.getStatus() == ParticipationStatus.COMPLETED) {
                    completedCount++;
                }
                if (record.getServiceRating() != null) {
                    ratedCount++;
                    if (record.getServiceRating() >= 4) {
                        positiveRatingCount++;
                    }
                }
                int recordTrackCount = trackPointMap.getOrDefault(record.getId(), new ArrayList<>()).size();
                totalTrackPoints += recordTrackCount;
                activityTrackPoints += recordTrackCount;
            }
            if (activityTrackPoints > 0) {
                activitiesWithTracks++;
            }
        }

        int totalCapacity = activities.stream()
                .mapToInt(VolunteerActivity::getCapacity)
                .sum();
        double trackCoverageRate = activities.isEmpty() ? 0 : (activitiesWithTracks * 100D) / activities.size();
        double averageTrackPoints = activities.isEmpty() ? 0 : totalTrackPoints * 1.0 / activities.size();
        double normalizedTrackDensity = Math.min(100D, averageTrackPoints * 10D);
        double trackCoverageScore = round(trackCoverageRate * 0.6 + normalizedTrackDensity * 0.4);

        double demandLevelScore = (demandLevel / 5D) * 35D;
        double gapRatioScore = totalCapacity <= 0 ? 0 : Math.min(100D, (gapCount * 100D) / totalCapacity) * 0.45;
        double lowTrackPenalty = (100D - trackCoverageScore) * 0.2;
        int demandIndex = clampScore(demandLevelScore + gapRatioScore + lowTrackPenalty);

        double completedScore = Math.min(100D, completedCount * 15D);
        double serviceHoursScore = Math.min(100D, totalHours * 6D);
        double uniqueUserScore = Math.min(100D, uniqueUsers.size() * 12D);
        double positiveRatingRate = ratedCount == 0 ? 0 : (positiveRatingCount * 100D) / ratedCount;
        int vitalityIndex = clampScore(
                completedScore * 0.35
                        + serviceHoursScore * 0.25
                        + uniqueUserScore * 0.2
                        + trackCoverageScore * 0.1
                        + positiveRatingRate * 0.1
        );
        String areaTag = resolveInsightTag(demandIndex, vitalityIndex, gapCount);

        CommunityInsightView view = new CommunityInsightView();
        view.setCommunityName(resolveCommunityName(anchor, site));
        view.setStreetName(resolveStreetName(site));
        view.setLocation(resolveInsightLocation(anchor, site));
        view.setLatitude(site == null ? anchor.getLatitude() : site.getLatitude());
        view.setLongitude(site == null ? anchor.getLongitude() : site.getLongitude());
        view.setActivityCount(activities.size());
        view.setTotalEnrollments(totalEnrollments);
        view.setGapCount(gapCount);
        view.setTrackCoverageScore(trackCoverageScore);
        view.setDemandIndex(demandIndex);
        view.setVitalityIndex(vitalityIndex);
        view.setAreaTag(areaTag);
        view.setGuidanceText(buildCommunityGuidance(areaTag, gapCount, totalEnrollments, totalHours));
        return view;
    }

    private String buildCommunityGuidance(String areaTag, int gapCount, int totalEnrollments, double totalHours) {
        if ("需求洼地".equals(areaTag)) {
            return "当前报名缺口 " + gapCount + " 人，建议优先补充志愿者与活动曝光。";
        }
        if ("服务活力区".equals(areaTag)) {
            return "累计报名 " + totalEnrollments + " 人，沉淀服务时长 " + round(totalHours) + " 小时，可作为优秀案例展示。";
        }
        return "供需相对平衡，可按活动类别继续做精细化分配。";
    }

    private String resolveCommunityKey(VolunteerActivity activity, ServiceSite site) {
        String communityName = resolveCommunityName(activity, site);
        String streetName = resolveStreetName(site);
        return communityName + "|" + streetName;
    }

    private String resolveCommunityName(VolunteerActivity activity, ServiceSite site) {
        if (site != null && StringUtils.hasText(site.getCommunityName())) {
            return site.getCommunityName();
        }
        return resolveAreaLabel(activity.getLocation());
    }

    private String resolveStreetName(ServiceSite site) {
        if (site != null && StringUtils.hasText(site.getStreetName())) {
            return site.getStreetName();
        }
        return "未标注街道";
    }

    private String resolveInsightLocation(VolunteerActivity activity, ServiceSite site) {
        if (site != null) {
            return buildServiceSiteLocation(site);
        }
        return StringUtils.hasText(activity.getLocation()) ? activity.getLocation() : "未标注地点";
    }

    private String buildServiceSiteLocation(ServiceSite site) {
        if (site == null) {
            return "未标注地点";
        }
        String communityName = StringUtils.hasText(site.getCommunityName()) ? site.getCommunityName() : "未标注社区";
        String streetName = StringUtils.hasText(site.getStreetName()) ? site.getStreetName() : "未标注街道";
        String address = StringUtils.hasText(site.getAddress()) ? site.getAddress() : (StringUtils.hasText(site.getName()) ? site.getName() : "未标注地点");
        return communityName + " / " + streetName + " / " + address;
    }

    private String resolveInsightTag(int demandIndex, int vitalityIndex, int gapCount) {
        if (demandIndex >= 70 && gapCount >= 3) {
            return "需求洼地";
        }
        if (vitalityIndex >= 70 && vitalityIndex > demandIndex) {
            return "服务活力区";
        }
        return "均衡服务区";
    }

    private int clampScore(double value) {
        return Math.max(0, Math.min(100, (int) Math.round(value)));
    }

    private LocalDateTime resolvePeriodStart(String period) {
        LocalDateTime now = LocalDateTime.now();
        if (PERIOD_MONTH.equalsIgnoreCase(normalizeAnalyticsPeriod(period))) {
            return now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        int dayOffset = now.getDayOfWeek().getValue() - 1;
        return now.minusDays(dayOffset).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private boolean isInPeriod(LocalDateTime value, LocalDateTime startAt, LocalDateTime endAt) {
        return value != null && !value.isBefore(startAt) && !value.isAfter(endAt);
    }

    private String normalizeAnalyticsPeriod(String period) {
        if (PERIOD_MONTH.equalsIgnoreCase(period)) {
            return PERIOD_MONTH;
        }
        return PERIOD_WEEK;
    }

    private boolean containsActivity(List<VolunteerActivity> activities, Long activityId) {
        if (activities == null || activities.isEmpty() || activityId == null) {
            return false;
        }
        return activities.stream().anyMatch(activity -> activityId.equals(activity.getId()));
    }

    private List<AreaStatView> buildTopAreas(List<VolunteerActivity> activities, int limit) {
        return activities.stream()
                .collect(Collectors.groupingBy(
                        activity -> resolveAreaLabel(activity.getLocation()),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new AreaStatView(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private String resolveTopCategoryFromActivities(List<VolunteerActivity> activities) {
        return activities.stream()
                .map(activity -> normalizeActivityCategory(activity.getCategory()))
                .filter(StringUtils::hasText)
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }

    private List<HeatmapPointView> buildCommunityHeatmap(List<VolunteerActivity> activities) {
        Map<String, List<VolunteerActivity>> areaMap = activities.stream()
                .collect(Collectors.groupingBy(activity -> resolveAreaLabel(activity.getLocation())));
        List<HeatmapPointView> items = new ArrayList<>();
        for (Map.Entry<String, List<VolunteerActivity>> entry : areaMap.entrySet()) {
            List<VolunteerActivity> areaActivities = entry.getValue();
            if (areaActivities.isEmpty()) {
                continue;
            }
            VolunteerActivity anchor = areaActivities.get(0);
            HeatmapPointView view = new HeatmapPointView();
            view.setActivityId(anchor.getId());
            view.setActivityTitle(entry.getKey());
            view.setLocation(entry.getKey());
            view.setCategory("社区服务热区");
            view.setLatitude(anchor.getLatitude());
            view.setLongitude(anchor.getLongitude());
            view.setIntensity(areaActivities.stream().mapToInt(activity -> Math.max(activity.getEnrolledCount(), 1)).sum());
            view.setDemandLevel(areaActivities.stream()
                    .map(VolunteerActivity::getDemandLevel)
                    .filter(item -> item != null)
                    .max(Integer::compareTo)
                    .orElse(3));
            view.setGapCount(areaActivities.stream()
                    .mapToInt(activity -> Math.max(0, activity.getCapacity() - activity.getEnrolledCount()))
                    .sum());
            view.setAreaTag("覆盖 " + areaActivities.size() + " 场活动");
            items.add(view);
        }
        return items.stream()
                .sorted(Comparator.comparing(HeatmapPointView::getIntensity).reversed())
                .collect(Collectors.toList());
    }

    private List<HeatmapPointView> buildActivityHeatmap(List<VolunteerActivity> activities) {
        return activities.stream()
                .sorted(Comparator.comparing(VolunteerActivity::getEnrolledCount).reversed()
                        .thenComparing(VolunteerActivity::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(activity -> {
                    int gapCount = Math.max(0, activity.getCapacity() - activity.getEnrolledCount());
                    int demandLevel = resolveDemandLevel(activity.getDemandLevel());
                    HeatmapPointView view = new HeatmapPointView();
                    view.setActivityId(activity.getId());
                    view.setActivityTitle(activity.getTitle());
                    view.setLocation(activity.getLocation());
                    view.setCategory(normalizeActivityCategory(activity.getCategory()));
                    view.setLatitude(activity.getLatitude());
                    view.setLongitude(activity.getLongitude());
                    view.setIntensity(activity.getEnrolledCount() * 10 + demandLevel * 5 + Math.max(0, activity.getCapacity() / 2 - gapCount));
                    view.setDemandLevel(demandLevel);
                    view.setGapCount(gapCount);
                    view.setAreaTag(resolveAreaTag(activity, gapCount, demandLevel));
                    return view;
                })
                .collect(Collectors.toList());
    }

    private List<ActivityRecordView> buildRepresentativeManagedRecords(List<ParticipationRecord> records, UserProfile admin) {
        if (records == null || records.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, VolunteerActivity> activityMap = loadActivityMap(records);
        return records.stream()
                .sorted(Comparator.comparing(ParticipationRecord::getServiceHours).reversed()
                        .thenComparing(record -> record.getEarnedPoints(), Comparator.reverseOrder())
                        .thenComparing(this::resolveRecordTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(record -> {
                    VolunteerActivity activity = activityMap.get(record.getActivityId());
                    if (activity == null) {
                        return null;
                    }
                    UserProfile participant = getUser(record.getUserId());
                    return buildActivityRecordView(record, activity, participant == null ? admin : participant);
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }

    private String buildAdminAnnualSummary(AdminAnnualReportView view) {
        return view.getYear()
                + " 年，你组织了 "
                + view.getTotalActivities()
                + " 场活动，累计带动 "
                + view.getTotalServiceCount()
                + " 人次服务，沉淀 "
                + view.getTotalServiceHours()
                + " 小时志愿时长。";
    }

    private AdminLeaderboardView buildAdminLeaderboardView(UserProfile admin, int targetYear) {
        List<VolunteerActivity> managedActivities = listManagedActivities(admin.getId(), targetYear);
        List<ParticipationRecord> managedRecords = participationRecordRepository.findAll().stream()
                .filter(this::isEffectiveParticipationRecord)
                .filter(record -> isInYear(resolveRecordTime(record), targetYear))
                .filter(record -> containsActivity(managedActivities, record.getActivityId()))
                .collect(Collectors.toList());

        AdminLeaderboardView view = new AdminLeaderboardView();
        view.setUserId(admin.getId());
        view.setUserName(resolveDisplayName(admin));
        view.setTotalActivities(managedActivities.size());
        view.setTotalServiceCount(managedRecords.size());
        view.setTotalServiceHours(round(managedRecords.stream().mapToDouble(ParticipationRecord::getServiceHours).sum()));
        view.setCoveredCommunityCount(buildTopAreas(managedActivities, Integer.MAX_VALUE).size());
        return view;
    }

    private ActivityRecordView buildActivityRecordView(ParticipationRecord record,
                                                       VolunteerActivity activity,
                                                       UserProfile user) {
        List<ServiceSnapshot> snapshots = serviceSnapshotRepository
                .findByActivityIdAndUserIdOrderByCreatedAtDesc(record.getActivityId(), record.getUserId());
        List<TrackPoint> trackPoints = trackPointRepository
                .findByActivityIdAndUserIdOrderByRecordedAtAsc(record.getActivityId(), record.getUserId());
        int snapshotCount = snapshots.size();
        int trackPointCount = trackPoints.size();

        ActivityRecordView view = new ActivityRecordView();
        view.setRecordId(record.getId());
        view.setActivityId(record.getActivityId());
        view.setActivityTitle(activity.getTitle());
        view.setActivityCategory(normalizeActivityCategory(activity.getCategory()));
        view.setUserId(record.getUserId());
        view.setUserName(resolveDisplayName(user));
        view.setStatus(record.getStatus());
        view.setCreatedAt(record.getCreatedAt());
        view.setCheckInTime(record.getCheckInTime());
        view.setCheckOutTime(record.getCheckOutTime());
        view.setServiceHours(record.getServiceHours());
        view.setEarnedPoints(record.getEarnedPoints());
        view.setServiceRating(record.getServiceRating());
        view.setServiceComment(record.getServiceComment());
        view.setPointBreakdown(record.getPointBreakdown());
        view.setRewardFinalizedAt(record.getRewardFinalizedAt());
        view.setOrganizerConfirmed(record.isOrganizerConfirmed());
        view.setOrganizerConfirmedAt(record.getOrganizerConfirmedAt());
        view.setOrganizerConfirmComment(record.getOrganizerConfirmComment());
        view.setRewardPending(
                record.getStatus() == ParticipationStatus.COMPLETED
                        && record.getRewardFinalizedAt() == null
                        && (record.getCertificateUrl() == null || record.getCertificateUrl().trim().isEmpty())
        );
        view.setCertificateUrl(record.getCertificateUrl());
        view.setEvidenceHash(record.getEvidenceHash());
        applyLatestBlockchainProof(view, record.getId());
        view.setEvidenceValid(isEvidenceValid(record, activity, user));
        view.setSnapshotCount(snapshotCount);
        view.setTrackPointCount(trackPointCount);
        view.setAnomalyReason(buildAnomalyReason(record, user, view));
        view.setAnomalyRemark(record.getAnomalyRemark());
        if (isAnomalyRecord(view)) {
            view.setAnomalyStatus(record.getAnomalyStatus() == null ? AnomalyStatus.PENDING : record.getAnomalyStatus());
        } else {
            view.setAnomalyStatus(record.getAnomalyStatus());
        }
        return view;
    }

    private void applyLatestBlockchainProof(ActivityRecordView view, Long recordId) {
        List<BlockchainProofRecord> proofs = blockchainProofRecordRepository.findByRecordIdOrderByCreatedAtDesc(recordId);
        if (proofs.isEmpty()) {
            return;
        }
        BlockchainProofRecord latest = proofs.get(0);
        view.setBlockchainProofStatus(latest.getProofStatus());
        view.setBlockchainAnchoredAt(latest.getAnchoredAt());
        view.setBlockchainTransactionNo(latest.getTransactionNo());
        view.setBlockchainProviderName(latest.getProviderName());
    }

    private void applyLatestBlockchainProof(ServiceCertificateView view, Long recordId) {
        List<BlockchainProofRecord> proofs = blockchainProofRecordRepository.findByRecordIdOrderByCreatedAtDesc(recordId);
        if (proofs.isEmpty()) {
            return;
        }
        BlockchainProofRecord latest = proofs.get(0);
        view.setBlockchainProofStatus(latest.getProofStatus());
        view.setBlockchainAnchoredAt(latest.getAnchoredAt());
        view.setBlockchainTransactionNo(latest.getTransactionNo());
        view.setBlockchainProviderName(latest.getProviderName());
    }

    private ServiceCertificateView.SnapshotItem buildCertificateSnapshotView(ServiceSnapshot snapshot) {
        ServiceCertificateView.SnapshotItem item = new ServiceCertificateView.SnapshotItem();
        item.setImageUrl(snapshot.getImageUrl());
        item.setNote(snapshot.getNote());
        item.setCreatedAt(snapshot.getCreatedAt());
        return item;
    }

    private BlockchainProofView buildBlockchainProofView(BlockchainProofRecord record) {
        BlockchainProofView view = new BlockchainProofView();
        view.setId(record.getId());
        view.setActivityId(record.getActivityId());
        view.setRecordId(record.getRecordId());
        view.setUserId(record.getUserId());
        view.setUserName(resolveDisplayName(getUser(record.getUserId())));
        view.setEventType(record.getEventType());
        view.setProofStatus(record.getProofStatus());
        view.setTransactionNo(record.getTransactionNo());
        view.setEventTime(record.getEventTime());
        view.setAnchoredAt(record.getAnchoredAt());
        view.setPayloadSummary(record.getPayloadSummary());
        view.setEvidenceHash(record.getEvidenceHash());
        view.setProviderName(record.getProviderName());
        view.setFailureReason(record.getFailureReason());
        return view;
    }

    private boolean isAnomalyRecord(ActivityRecordView view) {
        if (view.getStatus() != ParticipationStatus.COMPLETED) {
            return false;
        }
        if (view.isRewardPending()) {
            return false;
        }
        return StringUtils.hasText(view.getAnomalyReason());
    }

    private String buildAnomalyReason(ActivityRecordView view) {
        if (view.getStatus() != ParticipationStatus.COMPLETED) {
            return "";
        }
        if (view.isRewardPending()) {
            return "";
        }
        List<String> reasons = new ArrayList<>();
        if (!view.isEvidenceValid()) {
            reasons.add("\u5b58\u8bc1\u6821\u9a8c\u672a\u901a\u8fc7");
        }
        if (view.getSnapshotCount() <= 0) {
            reasons.add("\u7f3a\u5c11\u670d\u52a1\u5feb\u7167");
        }
        if (view.getTrackPointCount() <= 0) {
            reasons.add("\u7f3a\u5c11\u8f68\u8ff9\u70b9");
        }
        return String.join("\uff1b", reasons);
    }

    private String buildAnomalyReason(ParticipationRecord record, UserProfile user, ActivityRecordView view) {
        if (view.getStatus() != ParticipationStatus.COMPLETED) {
            return "";
        }
        if (view.isRewardPending()) {
            return "";
        }
        int minTrackPoints = systemSettingService.getMinTrackPoints();
        int minSnapshotCount = systemSettingService.getMinSnapshotCount();
        double minServiceHours = systemSettingService.getMinServiceHours();
        int frequentSupplementLimit = systemSettingService.getFrequentSupplementLimit30Days();
        List<String> reasons = new ArrayList<>();
        if (!view.isEvidenceValid()) {
            reasons.add("\u5b58\u8bc1\u6821\u9a8c\u672a\u901a\u8fc7");
        }
        if (view.getSnapshotCount() < minSnapshotCount) {
            reasons.add("\u7f3a\u5c11\u670d\u52a1\u5feb\u7167");
        }
        if (view.getTrackPointCount() < minTrackPoints) {
            reasons.add("\u8f68\u8ff9\u70b9\u8fc7\u5c11");
        }
        if (record.getServiceHours() < minServiceHours) {
            reasons.add("\u670d\u52a1\u65f6\u957f\u5f02\u5e38\u77ed");
        }
        if (hasFrequentSupplements(user.getId(), frequentSupplementLimit)) {
            reasons.add("\u8fd130\u5929\u8865\u7b7e\u9891\u7e41");
        }
        return String.join("\uff1b", reasons);
    }

    private boolean isEvidenceValid(ParticipationRecord record, VolunteerActivity activity, UserProfile user) {
        if (record.getStatus() != ParticipationStatus.COMPLETED) {
            return false;
        }
        if (record.getRewardFinalizedAt() == null
                && (record.getCertificateUrl() == null || record.getCertificateUrl().trim().isEmpty())
                && record.getEarnedPoints() <= 0) {
            return false;
        }
        if (record.getEvidenceHash() == null || record.getEvidenceHash().trim().isEmpty()) {
            return false;
        }
        return record.getEvidenceHash().equals(buildEvidenceHash(record, activity, user));
    }

    private boolean hasFrequentSupplements(Long userId, int threshold) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(FREQUENT_SUPPLEMENT_DAYS);
        long count = supplementApplicationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(item -> item.getCreatedAt() != null && !item.getCreatedAt().isBefore(cutoff))
                .count();
        return count >= threshold;
    }

    private int backfillBlockchainProofsForRecord(VolunteerActivity activity, ParticipationRecord record) {
        UserProfile user = getUser(record.getUserId());
        List<ServiceSnapshot> snapshots = serviceSnapshotRepository.findByActivityIdAndUserIdOrderByCreatedAtDesc(
                activity.getId(),
                user.getId()
        ).stream()
                .sorted(Comparator.comparing(ServiceSnapshot::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
        List<TrackPoint> trackPoints = trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(
                activity.getId(),
                user.getId()
        );
        TrackPoint startPoint = pickTrackPoint(trackPoints, TrackPointType.START, true);
        TrackPoint endPoint = pickTrackPoint(trackPoints, TrackPointType.END, false);

        int createdCount = 0;
        if (record.getCheckInTime() != null
                && !blockchainProofRecordRepository.existsByRecordIdAndEventType(record.getId(), BlockchainEventType.CHECK_IN)) {
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.CHECK_IN,
                    buildGenericHash("CHECK_IN|" + record.getId() + "|" + record.getCheckInTime() + "|"
                            + record.getCheckInMethod() + "|" + resolveLatitude(startPoint, record.getLastLatitude())
                            + "|" + resolveLongitude(startPoint, record.getLastLongitude())),
                    "\u8865\u5f55\u7b7e\u5230\u5b58\u8bc1\uff0c\u65b9\u5f0f\uff1a" + (record.getCheckInMethod() == null ? "-" : record.getCheckInMethod())
                            + "\uff0c\u5750\u6807\uff1a" + formatPointText(startPoint, record.getLastLatitude(), record.getLastLongitude()),
                    record.getCheckInTime()
            );
        }

        for (ServiceSnapshot snapshot : snapshots) {
            if (snapshot.getCreatedAt() == null) {
                continue;
            }
            if (blockchainProofRecordRepository.existsByRecordIdAndEventTypeAndEventTime(
                    record.getId(),
                    BlockchainEventType.SNAPSHOT_UPLOAD,
                    snapshot.getCreatedAt()
            )) {
                continue;
            }
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.SNAPSHOT_UPLOAD,
                    buildGenericHash("SNAPSHOT|" + record.getId() + "|" + snapshot.getId() + "|" + snapshot.getCreatedAt() + "|" + snapshot.getImageUrl()),
                    "\u8865\u5f55\u5feb\u7167\u5b58\u8bc1\uff0c\u5feb\u7167\u7f16\u53f7\uff1a" + snapshot.getId(),
                    snapshot.getCreatedAt()
            );
        }

        if (record.getCheckOutTime() != null
                && !blockchainProofRecordRepository.existsByRecordIdAndEventType(record.getId(), BlockchainEventType.CHECK_OUT)) {
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.CHECK_OUT,
                    buildGenericHash("CHECK_OUT|" + record.getId() + "|" + record.getCheckOutTime() + "|" + record.getServiceHours()
                            + "|" + resolveLatitude(endPoint, record.getLastLatitude())
                            + "|" + resolveLongitude(endPoint, record.getLastLongitude())),
                    "\u8865\u5f55\u7b7e\u9000\u5b58\u8bc1\uff0c\u670d\u52a1\u65f6\u957f\uff1a" + record.getServiceHours() + " \u5c0f\u65f6\uff0c\u5750\u6807\uff1a"
                            + formatPointText(endPoint, record.getLastLatitude(), record.getLastLongitude()),
                    record.getCheckOutTime()
            );
        }

        LocalDateTime summaryTime = record.getCheckOutTime() != null
                ? record.getCheckOutTime()
                : (trackPoints.isEmpty() ? null : trackPoints.get(trackPoints.size() - 1).getRecordedAt());
        if (summaryTime != null
                && !trackPoints.isEmpty()
                && !blockchainProofRecordRepository.existsByRecordIdAndEventType(record.getId(), BlockchainEventType.TRACK_SUMMARY)) {
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.TRACK_SUMMARY,
                    buildGenericHash("TRACK_SUMMARY|" + record.getId() + "|" + summarizeTrackPoints(trackPoints)),
                    summarizeTrackPoints(trackPoints),
                    summaryTime
            );
        }

        if (record.isOrganizerConfirmed()
                && record.getOrganizerConfirmedAt() != null
                && !blockchainProofRecordRepository.existsByRecordIdAndEventType(record.getId(), BlockchainEventType.ORGANIZER_CONFIRM)) {
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.ORGANIZER_CONFIRM,
                    buildGenericHash("ORGANIZER_CONFIRM|" + record.getId() + "|" + record.getOrganizerConfirmedAt()
                            + "|" + record.getServiceHours()),
                    "\u8865\u5f55\u7ec4\u7ec7\u8005\u786e\u8ba4\u5b58\u8bc1\uff0c\u670d\u52a1\u65f6\u957f\uff1a" + record.getServiceHours() + " \u5c0f\u65f6",
                    record.getOrganizerConfirmedAt()
            );
        }

        if (record.getRewardFinalizedAt() != null
                && !blockchainProofRecordRepository.existsByRecordIdAndEventType(record.getId(), BlockchainEventType.REWARD_FINALIZE)) {
            String evidenceHash = StringUtils.hasText(record.getEvidenceHash())
                    ? record.getEvidenceHash()
                    : buildEvidenceHash(record, activity, user);
            createdCount += saveBackfilledBlockchainProof(
                    record,
                    activity,
                    user,
                    BlockchainEventType.REWARD_FINALIZE,
                    evidenceHash,
                    "\u8865\u5f55\u8bc4\u4ef7\u7ed3\u7b97\u5b58\u8bc1\uff0c\u8bc4\u5206\uff1a" + (record.getServiceRating() == null ? "-" : record.getServiceRating())
                            + "\uff0c\u79ef\u5206\uff1a" + record.getEarnedPoints(),
                    record.getRewardFinalizedAt()
            );
        }
        return createdCount;
    }

    private void saveBlockchainProof(ParticipationRecord record,
                                     VolunteerActivity activity,
                                     UserProfile user,
                                     BlockchainEventType eventType,
                                     String evidenceHash,
                                     String payloadSummary,
                                     LocalDateTime eventTime) {
        BlockchainAnchorResult anchorResult = blockchainProofService.anchor(
                activity.getId(),
                record.getId(),
                user.getId(),
                eventType,
                evidenceHash,
                payloadSummary,
                eventTime
        );
        BlockchainProofRecord proofRecord = new BlockchainProofRecord();
        proofRecord.setActivityId(activity.getId());
        proofRecord.setRecordId(record.getId());
        proofRecord.setUserId(user.getId());
        proofRecord.setEventType(eventType);
        proofRecord.setEvidenceHash(evidenceHash);
        proofRecord.setProofStatus(anchorResult.getProofStatus());
        proofRecord.setTransactionNo(anchorResult.getTransactionNo());
        proofRecord.setEventTime(eventTime);
        proofRecord.setAnchoredAt(anchorResult.getAnchoredAt());
        proofRecord.setPayloadSummary(payloadSummary);
        proofRecord.setProviderName(anchorResult.getProviderName());
        proofRecord.setFailureReason(anchorResult.getFailureReason());
        proofRecord.setCreatedAt(LocalDateTime.now());
        blockchainProofRecordRepository.save(proofRecord);
    }

    private int saveBackfilledBlockchainProof(ParticipationRecord record,
                                              VolunteerActivity activity,
                                              UserProfile user,
                                              BlockchainEventType eventType,
                                              String evidenceHash,
                                              String payloadSummary,
                                              LocalDateTime eventTime) {
        if (eventTime == null) {
            return 0;
        }
        saveBlockchainProof(record, activity, user, eventType, evidenceHash, payloadSummary, eventTime);
        return 1;
    }

    private String summarizeTrackPoints(List<TrackPoint> trackPoints) {
        if (trackPoints == null || trackPoints.isEmpty()) {
            return "\u6682\u65e0\u8f68\u8ff9\u6458\u8981";
        }
        TrackPoint start = trackPoints.get(0);
        TrackPoint end = trackPoints.get(trackPoints.size() - 1);
        long stayCount = trackPoints.stream()
                .filter(item -> item.getPointType() == TrackPointType.MIDDLE)
                .count();
        return "\u8d77\u70b9\uff1a" + start.getLatitude() + "," + start.getLongitude()
                + "\uff0c\u7ec8\u70b9\uff1a" + end.getLatitude() + "," + end.getLongitude()
                + "\uff0c\u8f68\u8ff9\u70b9\u6570\uff1a" + trackPoints.size()
                + "\uff0c\u505c\u7559\u70b9\u6570\uff1a" + stayCount;
    }

    private TrackPoint pickTrackPoint(List<TrackPoint> trackPoints, TrackPointType pointType, boolean fallbackToFirst) {
        if (trackPoints == null || trackPoints.isEmpty()) {
            return null;
        }
        for (TrackPoint item : trackPoints) {
            if (item.getPointType() == pointType) {
                return item;
            }
        }
        return fallbackToFirst ? trackPoints.get(0) : trackPoints.get(trackPoints.size() - 1);
    }

    private Double resolveLatitude(TrackPoint point, Double fallback) {
        return point != null && point.getLatitude() != null ? point.getLatitude() : maskCoordinate(fallback);
    }

    private Double resolveLongitude(TrackPoint point, Double fallback) {
        return point != null && point.getLongitude() != null ? point.getLongitude() : maskCoordinate(fallback);
    }

    private String formatPointText(TrackPoint point, Double fallbackLatitude, Double fallbackLongitude) {
        Double latitude = resolveLatitude(point, fallbackLatitude);
        Double longitude = resolveLongitude(point, fallbackLongitude);
        return latitude == null || longitude == null ? "-" : latitude + ", " + longitude;
    }

    public ParticipationRecord enrollForParticipant(Long activityId, Long userId) {
        VolunteerActivity activity = getActivity(activityId);
        UserProfile user = getUser(userId);
        ensureActivityParticipant(user);
        identityVerificationService.ensureVerified(user, "\u4ee3\u7528\u6237\u62a5\u540d\u6d3b\u52a8");
        if (activity.getEnrolledCount() >= activity.getCapacity()) {
            throw new IllegalArgumentException("\u6d3b\u52a8\u540d\u989d\u5df2\u6ee1");
        }
        if (participationRecordRepository.existsByActivityIdAndUserId(activityId, userId)) {
            throw new IllegalArgumentException("\u4f60\u5df2\u7ecf\u62a5\u540d\u8fc7\u8be5\u6d3b\u52a8");
        }

        ParticipationRecord record = new ParticipationRecord();
        record.setActivityId(activityId);
        record.setUserId(userId);
        record.setStatus(ParticipationStatus.PENDING);
        record.setCreatedAt(LocalDateTime.now());
        ParticipationRecord saved = participationRecordRepository.save(record);

        activity.setEnrolledCount(activity.getEnrolledCount() + 1);
        volunteerActivityRepository.save(activity);
        return saved;
    }

    public ParticipationRecord checkInForParticipant(Long activityId, Long userId, CheckInRequest request) {
        ensureActivityParticipant(getUser(userId));
        return checkIn(activityId, userId, request);
    }

    public ParticipationRecord checkOutForParticipant(Long activityId, Long userId, CheckOutRequest request) {
        ensureActivityParticipant(getUser(userId));
        return checkOut(activityId, userId, request);
    }

    @Transactional(readOnly = true)
    public UserDashboardView getParticipantDashboard(Long userId) {
        UserProfile user = getUser(userId);
        ensureActivityParticipant(user);
        return getDashboard(userId);
    }

    private void enrichParticipationRecords(List<ParticipationRecord> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        Set<Long> activityIds = records.stream()
                .map(ParticipationRecord::getActivityId)
                .collect(Collectors.toSet());
        Map<Long, VolunteerActivity> activityMap = volunteerActivityRepository.findAllById(activityIds)
                .stream()
                .collect(Collectors.toMap(VolunteerActivity::getId, activity -> activity));
        records.forEach(record -> {
            VolunteerActivity activity = activityMap.get(record.getActivityId());
            if (activity == null) {
                return;
            }
            record.setActivityTitle(activity.getTitle());
            record.setActivityCategory(normalizeActivityCategory(activity.getCategory()));
        });
    }

    private int resolveTargetYear(Integer year) {
        int currentYear = LocalDateTime.now().getYear();
        if (year == null || year < 2000 || year > currentYear + 1) {
            return currentYear;
        }
        return year;
    }

    private LocalDateTime resolveRecordTime(ParticipationRecord record) {
        if (record.getCheckOutTime() != null) {
            return record.getCheckOutTime();
        }
        if (record.getCheckInTime() != null) {
            return record.getCheckInTime();
        }
        return record.getCreatedAt();
    }

    private boolean isInYear(LocalDateTime value, int year) {
        return value != null && value.getYear() == year;
    }

    private Map<Long, VolunteerActivity> loadActivityMap(List<ParticipationRecord> records) {
        if (records == null || records.isEmpty()) {
            return new HashMap<>();
        }
        Set<Long> activityIds = records.stream()
                .map(ParticipationRecord::getActivityId)
                .collect(Collectors.toSet());
        return volunteerActivityRepository.findAllById(activityIds).stream()
                .collect(Collectors.toMap(VolunteerActivity::getId, activity -> activity));
    }

    private String resolveTopCategory(List<ParticipationRecord> records) {
        return records.stream()
                .map(ParticipationRecord::getActivityCategory)
                .filter(StringUtils::hasText)
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("\u672a\u5206\u7c7b\u670d\u52a1");
    }

    private List<String> resolveCoveredAreas(List<ParticipationRecord> records, Map<Long, VolunteerActivity> activityMap) {
        return records.stream()
                .map(record -> activityMap.get(record.getActivityId()))
                .filter(activity -> activity != null && StringUtils.hasText(activity.getLocation()))
                .map(activity -> resolveAreaLabel(activity.getLocation()))
                .filter(StringUtils::hasText)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(LinkedHashSet::new),
                        ArrayList::new
                ));
    }

    private List<AnnualFootprintPointView> buildAnnualFootprints(List<ParticipationRecord> records,
                                                                 Map<Long, VolunteerActivity> activityMap) {
        List<AnnualFootprintPointView> footprints = new ArrayList<>();
        List<ParticipationRecord> sortedRecords = records.stream()
                .sorted(Comparator.comparing(this::resolveRecordTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        for (ParticipationRecord record : sortedRecords) {
            VolunteerActivity activity = activityMap.get(record.getActivityId());
            List<TrackPoint> trackPoints = trackPointRepository.findByActivityIdAndUserIdOrderByRecordedAtAsc(
                    record.getActivityId(),
                    record.getUserId()
            );
            if (!trackPoints.isEmpty()) {
                for (TrackPoint trackPoint : trackPoints) {
                    AnnualFootprintPointView pointView = new AnnualFootprintPointView();
                    pointView.setActivityId(record.getActivityId());
                    pointView.setActivityTitle(record.getActivityTitle());
                    pointView.setLocation(activity == null ? "" : activity.getLocation());
                    pointView.setLatitude(trackPoint.getLatitude());
                    pointView.setLongitude(trackPoint.getLongitude());
                    pointView.setPointType(trackPoint.getPointType() == null ? "SITE" : trackPoint.getPointType().name());
                    pointView.setRecordedAt(trackPoint.getRecordedAt());
                    footprints.add(pointView);
                }
                continue;
            }
            if (activity == null) {
                continue;
            }
            AnnualFootprintPointView pointView = new AnnualFootprintPointView();
            pointView.setActivityId(record.getActivityId());
            pointView.setActivityTitle(record.getActivityTitle());
            pointView.setLocation(activity.getLocation());
            pointView.setLatitude(activity.getLatitude());
            pointView.setLongitude(activity.getLongitude());
            pointView.setPointType("SITE");
            pointView.setRecordedAt(resolveRecordTime(record));
            footprints.add(pointView);
        }
        return footprints;
    }

    private List<ActivityRecordView> buildRepresentativeRecords(List<ParticipationRecord> records,
                                                                Map<Long, VolunteerActivity> activityMap,
                                                                UserProfile user) {
        return records.stream()
                .sorted(Comparator
                        .comparing(ParticipationRecord::getServiceRating, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(ParticipationRecord::getServiceHours, Comparator.reverseOrder())
                        .thenComparing(this::resolveRecordTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(3)
                .map(record -> {
                    VolunteerActivity activity = activityMap.get(record.getActivityId());
                    if (activity == null) {
                        return null;
                    }
                    return buildActivityRecordView(record, activity, user);
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }

    private String buildAnnualSummary(VolunteerAnnualReportView report) {
        return report.getYear()
                + " \u5e74\uff0c\u4f60\u53c2\u4e0e\u4e86 "
                + report.getTotalActivities()
                + " \u573a\u5fd7\u613f\u6d3b\u52a8\uff0c\u7d2f\u8ba1\u670d\u52a1 "
                + report.getTotalServiceHours()
                + " \u5c0f\u65f6\uff0c\u83b7\u5f97 "
                + report.getTotalPoints()
                + " \u79ef\u5206\u3002";
    }

    private String resolveAreaLabel(String location) {
        if (!StringUtils.hasText(location)) {
            return "\u672a\u6807\u6ce8\u533a\u57df";
        }
        int districtIndex = location.indexOf("\u533a");
        if (districtIndex >= 0) {
            return location.substring(Math.max(0, districtIndex - 4), districtIndex + 1);
        }
        int countyIndex = location.indexOf("\u53bf");
        if (countyIndex >= 0) {
            return location.substring(Math.max(0, countyIndex - 4), countyIndex + 1);
        }
        int cityIndex = location.indexOf("\u5e02");
        if (cityIndex >= 0) {
            return location.substring(Math.max(0, cityIndex - 4), cityIndex + 1);
        }
        return location.length() <= 6 ? location : location.substring(0, 6);
    }

    private Integer resolveLeaderboardRank(Long userId) {
        List<UserProfile> rankedUsers = userProfileRepository.findAll().stream()
                .filter(user -> user.getRole() == UserRole.VOLUNTEER || user.getRole() == UserRole.ADMIN)
                .sorted(Comparator.comparing(UserProfile::getTotalPoints).reversed()
                        .thenComparing(UserProfile::getTotalHours).reversed()
                        .thenComparing(UserProfile::getId))
                .collect(Collectors.toList());
        for (int index = 0; index < rankedUsers.size(); index++) {
            if (rankedUsers.get(index).getId().equals(userId)) {
                return index + 1;
            }
        }
        return null;
    }

    private boolean isMonitorableUser(UserProfile user) {
        return user != null
                && user.getRole() != null
                && (!StringUtils.hasText(user.getOpenId()) || !user.getOpenId().startsWith("system_"));
    }

    private boolean isEffectiveParticipationRecord(ParticipationRecord record) {
        if (record == null) {
            return false;
        }
        return record.getStatus() == ParticipationStatus.CHECKED_IN
                || record.getStatus() == ParticipationStatus.COMPLETED
                || record.getCheckInTime() != null
                || record.getCheckOutTime() != null
                || record.getServiceHours() > 0;
    }

    private List<AreaStatView> buildCategoryStats(List<VolunteerActivity> annualActivities) {
        return annualActivities.stream()
                .map(activity -> normalizeActivityCategory(activity.getCategory()))
                .filter(StringUtils::hasText)
                .collect(Collectors.groupingBy(item -> item, Collectors.collectingAndThen(Collectors.counting(), Long::intValue)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(6)
                .map(entry -> new AreaStatView(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private List<TrendStatView> buildMonthlyActivityTrends(List<VolunteerActivity> annualActivities, int year) {
        Map<Integer, Long> countByMonth = annualActivities.stream()
                .filter(activity -> activity.getStartTime() != null && activity.getStartTime().getYear() == year)
                .collect(Collectors.groupingBy(activity -> activity.getStartTime().getMonthValue(), Collectors.counting()));
        List<TrendStatView> items = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            items.add(new TrendStatView(month + "\u6708", countByMonth.getOrDefault(month, 0L)));
        }
        return items;
    }

    private List<TrendStatView> buildMonthlyServiceHourTrends(List<ParticipationRecord> annualRecords, int year) {
        Map<Integer, Double> hoursByMonth = annualRecords.stream()
                .filter(record -> resolveRecordTime(record) != null && resolveRecordTime(record).getYear() == year)
                .collect(Collectors.groupingBy(
                        record -> resolveRecordTime(record).getMonthValue(),
                        Collectors.summingDouble(ParticipationRecord::getServiceHours)
                ));
        List<TrendStatView> items = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            items.add(new TrendStatView(month + "\u6708", round(hoursByMonth.getOrDefault(month, 0D))));
        }
        return items;
    }
    private UserEvidenceSummaryView buildUserEvidenceSummary(UserProfile user, List<ParticipationRecord> records) {
        UserEvidenceSummaryView view = new UserEvidenceSummaryView();
        if (user == null || records == null || records.isEmpty()) {
            return view;
        }
        int snapshotCount = 0;
        int trackPointCount = 0;
        int blockchainProofCount = 0;
        int anomalyCount = 0;

        for (ParticipationRecord record : records) {
            snapshotCount += serviceSnapshotRepository
                    .findByActivityIdAndUserIdOrderByCreatedAtDesc(record.getActivityId(), user.getId())
                    .size();
            trackPointCount += trackPointRepository
                    .findByActivityIdAndUserIdOrderByRecordedAtAsc(record.getActivityId(), user.getId())
                    .size();
            blockchainProofCount += blockchainProofRecordRepository.findByRecordIdOrderByCreatedAtDesc(record.getId()).size();
            if (record.getAnomalyStatus() != null || StringUtils.hasText(record.getAnomalyRemark())) {
                anomalyCount++;
            }
        }

        view.setSnapshotCount(snapshotCount);
        view.setTrackPointCount(trackPointCount);
        view.setBlockchainProofCount(blockchainProofCount);
        view.setAnomalyCount(anomalyCount);
        return view;
    }

    private List<HeatmapPointView> buildRelatedHeatmapActivities(VolunteerAnnualReportView annualReport) {
        if (annualReport == null || annualReport.getCoveredAreas() == null || annualReport.getCoveredAreas().isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> coveredAreas = new LinkedHashSet<>(annualReport.getCoveredAreas());
        return buildSmartHeatmapForAdmin().stream()
                .filter(item -> coveredAreas.contains(resolveAreaLabel(item.getLocation())))
                .limit(6)
                .collect(Collectors.toList());
    }

    public ServiceSnapshot createSnapshotForParticipant(Long userId, CreateSnapshotRequest request) {
        ensureActivityParticipant(getUser(userId));
        return createSnapshot(userId, request);
    }

    @Transactional(readOnly = true)
    public List<ServiceSnapshot> listSnapshotsForParticipant(Long activityId, Long userId) {
        ensureActivityParticipant(getUser(userId));
        return listSnapshots(activityId, userId);
    }

    public TrackPoint recordTrackPointForParticipant(Long userId, TrackPointRequest request) {
        ensureActivityParticipant(getUser(userId));
        return recordTrackPoint(userId, request);
    }

    @Transactional(readOnly = true)
    public List<TrackPoint> listTrackPointsForParticipant(Long activityId, Long userId) {
        ensureActivityParticipant(getUser(userId));
        return listTrackPoints(activityId, userId);
    }

    public SupplementApplication applySupplementForParticipant(Long userId, SupplementApplyRequest request) {
        ensureActivityParticipant(getUser(userId));
        return applySupplement(userId, request);
    }

    @Transactional(readOnly = true)
    public List<SupplementApplication> listMySupplementsForParticipant(Long userId) {
        ensureActivityParticipant(getUser(userId));
        return listMySupplements(userId);
    }

    public RedemptionRecord redeemForParticipant(Long userId, RedeemRequest request) {
        UserProfile user = getUser(userId);
        ensureActivityParticipant(user);
        return redeem(userId, request);
    }

    @Transactional(readOnly = true)
    public List<RedemptionRecord> listMyRedemptionsForParticipant(Long userId) {
        ensureActivityParticipant(getUser(userId));
        return listMyRedemptions(userId);
    }

    @Transactional(readOnly = true)
    public List<RecommendedActivityView> recommendActivitiesForParticipant(Long userId) {
        ensureActivityParticipant(getUser(userId));
        return recommendActivitiesV2(userId);
    }

    private void ensureActivityParticipant(UserProfile user) {
        if (user == null || (user.getRole() != UserRole.VOLUNTEER && user.getRole() != UserRole.ADMIN)) {
            throw new IllegalArgumentException("\u53ea\u6709\u5fd7\u613f\u8005\u548c\u6d3b\u52a8\u7ba1\u7406\u5458\u624d\u80fd\u53c2\u4e0e\u6d3b\u52a8");
        }
    }

    private String resolveDisplayName(UserProfile user) {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty()) {
            return "\u5fae\u4fe1\u7528\u6237" + (user == null ? "" : user.getId());
        }
        String name = user.getName().trim();
        if (name.replace("?", "").isEmpty()) {
            return "\u5fae\u4fe1\u7528\u6237" + user.getId();
        }
        return name;
    }

    private boolean isWithinGeofence(VolunteerActivity activity, double latitude, double longitude) {
        double distance = distanceInMeters(activity.getLatitude(), activity.getLongitude(), latitude, longitude);
        return distance <= activity.getGeofenceRadiusMeters();
    }

    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private TrackPoint saveTrackPoint(Long activityId,
                                      Long userId,
                                      Double latitude,
                                      Double longitude,
                                      LocalDateTime recordedAt,
                                      TrackPointType pointType,
                                      boolean forceSave) {
        if (latitude == null || longitude == null) {
            return null;
        }

        TrackPoint nextPoint = new TrackPoint();
        nextPoint.setActivityId(activityId);
        nextPoint.setUserId(userId);
        nextPoint.setLatitude(maskCoordinate(latitude));
        nextPoint.setLongitude(maskCoordinate(longitude));
        nextPoint.setRecordedAt(recordedAt == null ? LocalDateTime.now() : recordedAt);
        nextPoint.setPointType(pointType == null ? TrackPointType.MIDDLE : pointType);

        if (!forceSave) {
            TrackPoint lastPoint = trackPointRepository.findTopByActivityIdAndUserIdOrderByRecordedAtDesc(activityId, userId).orElse(null);
            if (lastPoint != null) {
                long seconds = Math.abs(Duration.between(lastPoint.getRecordedAt(), nextPoint.getRecordedAt()).getSeconds());
                double distance = distanceInMeters(
                        lastPoint.getLatitude(),
                        lastPoint.getLongitude(),
                        nextPoint.getLatitude(),
                        nextPoint.getLongitude()
                );
                if (seconds < 60 && distance < 50 && lastPoint.getPointType() == nextPoint.getPointType()) {
                    return lastPoint;
                }
            }
        }
        return trackPointRepository.save(nextPoint);
    }

    private Double maskCoordinate(Double value) {
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }

    private String normalizeActivityCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return "\u7efc\u5408\u670d\u52a1";
        }
        return category.trim();
    }

    private int resolveDifficultyLevel(Integer difficultyLevel) {
        if (difficultyLevel == null || difficultyLevel < 1 || difficultyLevel > 5) {
            return 3;
        }
        return difficultyLevel;
    }

    private int resolveDemandLevel(Integer demandLevel) {
        if (demandLevel == null || demandLevel < 1 || demandLevel > 5) {
            return 3;
        }
        return demandLevel;
    }

    private String resolveAreaTag(VolunteerActivity activity, int gapCount, int demandLevel) {
        if (demandLevel >= 4 && gapCount >= Math.max(2, activity.getCapacity() / 3)) {
            return "\u8d44\u6e90\u6d3c\u5730";
        }
        if (activity.getEnrolledCount() >= Math.max(3, (int) Math.ceil(activity.getCapacity() * 0.8))) {
            return "\u6d3b\u529b\u533a";
        }
        return "\u5e38\u89c4\u670d\u52a1\u533a";
    }

    private int calculateRecommendationScore(UserProfile user, VolunteerActivity activity, List<String> preferredCategories) {
        int preferenceScore = preferredCategories.contains(normalizeActivityCategory(activity.getCategory())) ? 18 : 6;
        int demandScore = resolveDemandLevel(activity.getDemandLevel()) * 5;
        int gapScore = Math.max(0, activity.getCapacity() - activity.getEnrolledCount()) * 2;
        int difficultyScore = resolveDifficultyMatchScore(user, resolveDifficultyLevel(activity.getDifficultyLevel()));
        return preferenceScore + demandScore + gapScore + difficultyScore;
    }

    private int resolveDifficultyMatchScore(UserProfile user, int difficultyLevel) {
        if (user.getTotalHours() < 15) {
            return difficultyLevel <= 2 ? 8 : 2;
        }
        if (user.getTotalHours() < 60) {
            return difficultyLevel == 3 ? 8 : 5;
        }
        return difficultyLevel >= 4 ? 10 : 6;
    }

    private String resolveBadgeName(double totalHours) {
        if (totalHours >= 100) {
            return "\u5353\u8d8a\u5fd7\u613f\u8005";
        }
        if (totalHours >= 60) {
            return "\u5148\u950b\u5fd7\u613f\u8005";
        }
        if (totalHours >= 30) {
            return "\u6d3b\u529b\u5fd7\u613f\u8005";
        }
        if (totalHours >= 10) {
            return "\u6210\u957f\u5fd7\u613f\u8005";
        }
        return "\u65b0\u79c0\u5fd7\u613f\u8005";
    }

    private double calculateHours(LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = Duration.between(startTime, endTime).toMinutes();
        if (minutes <= 0) {
            return 0;
        }
        return round(minutes / 60.0);
    }

    private int calculatePoints(double serviceHours) {
        return (int) Math.round(serviceHours * 10 + 5);
    }

    private String resolveBadge(double totalHours) {
        if (totalHours >= 100) {
            return "\u5353\u8d8a\u5fd7\u613f\u8005";
        }
        if (totalHours >= 60) {
            return "\u5148\u950b\u5fd7\u613f\u8005";
        }
        if (totalHours >= 30) {
            return "\u6d3b\u529b\u5fd7\u613f\u8005";
        }
        if (totalHours >= 10) {
            return "\u6210\u957f\u5fd7\u613f\u8005";
        }
        return "\u65b0\u79c0\u5fd7\u613f\u8005";
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private String buildEvidenceHash(ParticipationRecord record, VolunteerActivity activity, UserProfile user) {
        String raw = record.getId() + "|" + user.getId() + "|" + activity.getId() + "|" + record.getCheckInTime() + "|" + record.getCheckOutTime()
                + "|" + record.getServiceHours() + "|" + record.getEarnedPoints() + "|" + record.getServiceRating()
                + "|" + record.getServiceComment() + "|" + normalizeActivityCategory(activity.getCategory());
        return buildGenericHash(raw);
    }

    private String buildGenericHash(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(String.format("%02x", aByte));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("\u751f\u6210\u5b58\u8bc1\u6458\u8981\u5931\u8d25");
        }
    }

    private String generateCertificateNo() {
        return "CERT-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}

