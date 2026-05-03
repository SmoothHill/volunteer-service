package com.yueping.volunteer.init;

import com.yueping.volunteer.dto.CreateActivityRequest;
import com.yueping.volunteer.model.StoreItem;
import com.yueping.volunteer.model.StoreItemDeliveryType;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.repository.AdminApplicationRepository;
import com.yueping.volunteer.repository.ParticipationRecordRepository;
import com.yueping.volunteer.repository.RedemptionRecordRepository;
import com.yueping.volunteer.repository.ServiceSnapshotRepository;
import com.yueping.volunteer.repository.StoreItemRepository;
import com.yueping.volunteer.repository.SupplementApplicationRepository;
import com.yueping.volunteer.repository.TrackPointRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import com.yueping.volunteer.repository.VolunteerActivityRepository;
import com.yueping.volunteer.service.AdminAuditLogService;
import com.yueping.volunteer.service.IdentityVerificationService;
import com.yueping.volunteer.service.SensitiveDataService;
import com.yueping.volunteer.service.VolunteerPlatformService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    private static final String UNASSIGNED_BADGE_NAME = "\u5f85\u9009\u62e9\u8eab\u4efd";
    private static final String VOLUNTEER_BADGE_NAME = "\u4e00\u661f\u5fd7\u613f\u8005";
    private static final String ADMIN_BADGE_NAME = "\u6d3b\u52a8\u7ba1\u7406\u5458";
    private static final String SUPER_ADMIN_BADGE_NAME = "\u7cfb\u7edf\u8d85\u7ea7\u7ba1\u7406\u5458";

    private final UserProfileRepository userProfileRepository;
    private final VolunteerActivityRepository volunteerActivityRepository;
    private final VolunteerPlatformService volunteerPlatformService;
    private final StoreItemRepository storeItemRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final SupplementApplicationRepository supplementApplicationRepository;
    private final ServiceSnapshotRepository serviceSnapshotRepository;
    private final TrackPointRepository trackPointRepository;
    private final RedemptionRecordRepository redemptionRecordRepository;
    private final AdminApplicationRepository adminApplicationRepository;
    private final AdminAuditLogService adminAuditLogService;
    private final IdentityVerificationService identityVerificationService;
    private final boolean demoDataEnabled;

    public DemoDataInitializer(UserProfileRepository userProfileRepository,
                               VolunteerActivityRepository volunteerActivityRepository,
                               VolunteerPlatformService volunteerPlatformService,
                               StoreItemRepository storeItemRepository,
                               ParticipationRecordRepository participationRecordRepository,
                               SupplementApplicationRepository supplementApplicationRepository,
                               ServiceSnapshotRepository serviceSnapshotRepository,
                               TrackPointRepository trackPointRepository,
                               RedemptionRecordRepository redemptionRecordRepository,
                               AdminApplicationRepository adminApplicationRepository,
                               AdminAuditLogService adminAuditLogService,
                               IdentityVerificationService identityVerificationService,
                               @Value("${app.demo-data.enabled:false}") boolean demoDataEnabled) {
        this.userProfileRepository = userProfileRepository;
        this.volunteerActivityRepository = volunteerActivityRepository;
        this.volunteerPlatformService = volunteerPlatformService;
        this.storeItemRepository = storeItemRepository;
        this.participationRecordRepository = participationRecordRepository;
        this.supplementApplicationRepository = supplementApplicationRepository;
        this.serviceSnapshotRepository = serviceSnapshotRepository;
        this.trackPointRepository = trackPointRepository;
        this.redemptionRecordRepository = redemptionRecordRepository;
        this.adminApplicationRepository = adminApplicationRepository;
        this.adminAuditLogService = adminAuditLogService;
        this.identityVerificationService = identityVerificationService;
        this.demoDataEnabled = demoDataEnabled;
    }

    @Override
    public void run(String... args) {
        if (!demoDataEnabled) {
            return;
        }
        initUsers();
        initActivities();
        initStoreItems();
    }

    public void initializeDemoData(Long operatorId) {
        ensureDemoDataEnabled();
        ensureVerifiedOperator(operatorId, "初始化演示数据");
        initActivities();
        initStoreItems();
    }

    @Transactional
    public void resetDemoData(Long operatorId) {
        ensureDemoDataEnabled();
        ensureVerifiedOperator(operatorId, "重置演示数据");
        clearBusinessData();
        resetUserSummaries();
        initActivities();
        initStoreItems();
        adminAuditLogService.log(
                operatorId,
                "DEMO_DATA_RESET",
                "SYSTEM",
                "DEMO_DATA",
                "\u91cd\u7f6e\u6f14\u793a\u6570\u636e\u5e76\u91cd\u65b0\u521d\u59cb\u5316\u793a\u4f8b\u6d3b\u52a8\u548c\u5546\u54c1"
        );
    }

    public void ensureDemoDataEnabled() {
        if (!demoDataEnabled) {
            throw new IllegalArgumentException("演示数据能力未开启");
        }
    }

    private void initUsers() {
        if (userProfileRepository.count() > 0) {
            return;
        }

        UserProfile volunteer = new UserProfile();
        volunteer.setOpenId("demo_volunteer");
        volunteer.setName("\u5cb3\u5e73");
        volunteer.setRole(UserRole.VOLUNTEER);
        volunteer.setAvatarUrl("");
        volunteer.setRealName("岳平");
        volunteer.setIdCardNo("510123199901011234");
        volunteer.setIdCardHash(SensitiveDataService.hashForLookup("510123199901011234"));
        volunteer.setVolunteerCardNo("V-demo-001");
        volunteer.setVerified(true);
        volunteer.setTotalHours(12.5);
        volunteer.setTotalPoints(180);
        volunteer.setCreditScore(96);
        volunteer.setBadgeName("\u56db\u661f\u5fd7\u613f\u8005");
        volunteer.setCreatedAt(LocalDateTime.now());
        userProfileRepository.save(volunteer);

        UserProfile admin = new UserProfile();
        admin.setOpenId("demo_admin");
        admin.setName("\u793e\u533a\u6d3b\u52a8\u7ba1\u7406\u5458");
        admin.setRole(UserRole.ADMIN);
        admin.setAvatarUrl("");
        admin.setRealName("社区活动管理员");
        admin.setIdCardNo("510123199902021234");
        admin.setIdCardHash(SensitiveDataService.hashForLookup("510123199902021234"));
        admin.setVolunteerCardNo("V-demo-002");
        admin.setVerified(true);
        admin.setTotalHours(0);
        admin.setTotalPoints(0);
        admin.setCreditScore(100);
        admin.setBadgeName("\u6d3b\u52a8\u7ba1\u7406\u5458");
        admin.setCreatedAt(LocalDateTime.now());
        userProfileRepository.save(admin);
    }

    private void initActivities() {
        if (volunteerActivityRepository.count() > 0) {
            return;
        }

        CreateActivityRequest request1 = new CreateActivityRequest();
        request1.setTitle("\u793e\u533a\u6e05\u6d01\u884c\u52a8");
        request1.setDescription("\u9488\u5bf9\u793e\u533a\u4e3b\u5e72\u9053\u548c\u7eff\u5316\u533a\u57df\u5f00\u5c55\u73af\u5883\u6e05\u6d01\uff0c\u652f\u6301\u53ef\u4fe1\u7b7e\u5230\u4e0e\u65f6\u957f\u8ba1\u91cf\u3002");
        request1.setLocation("\u9ad8\u65b0\u533a\u548c\u4e50\u793e\u533a\u670d\u52a1\u7ad9");
        request1.setLatitude(30.5728);
        request1.setLongitude(104.0668);
        request1.setStartTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0));
        request1.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(30).withSecond(0).withNano(0));
        request1.setCapacity(30);
        request1.setGeofenceRadiusMeters(300);
        Long demoAdminId = userProfileRepository.findByOpenId("demo_admin")
                .map(UserProfile::getId)
                .orElse(null);
        volunteerPlatformService.createActivity(request1, demoAdminId);

        CreateActivityRequest request2 = new CreateActivityRequest();
        request2.setTitle("\u72ec\u5c45\u8001\u4eba\u4e0a\u95e8\u5e2e\u6276");
        request2.setDescription("\u9762\u5411\u793e\u533a\u72ec\u5c45\u8001\u4eba\u5f00\u5c55\u966a\u4f34\u548c\u751f\u6d3b\u534f\u52a9\uff0c\u9002\u5408\u505a\u670d\u52a1\u8f68\u8ff9\u4e0e\u7167\u7247\u5b58\u8bc1\u3002");
        request2.setLocation("\u9526\u57ce\u793e\u533a\u5c45\u5bb6\u517b\u8001\u670d\u52a1\u4e2d\u5fc3");
        request2.setLatitude(30.5702);
        request2.setLongitude(104.0646);
        request2.setStartTime(LocalDateTime.now().plusDays(2).withHour(14).withMinute(0).withSecond(0).withNano(0));
        request2.setEndTime(LocalDateTime.now().plusDays(2).withHour(17).withMinute(0).withSecond(0).withNano(0));
        request2.setCapacity(12);
        request2.setGeofenceRadiusMeters(200);
        volunteerPlatformService.createActivity(request2, demoAdminId);
    }

    private void initStoreItems() {
        if (storeItemRepository.count() > 0) {
            return;
        }

        storeItemRepository.save(buildStoreItem(
                "\u7535\u5b50\u8363\u8a89\u8d34\u7eb8",
                "\u865a\u62df\u6743\u76ca",
                "\u5b8c\u6210\u5151\u6362\u540e\u53ef\u5728\u4e2a\u4eba\u4e2d\u5fc3\u5c55\u793a\u4e13\u5c5e\u8d34\u7eb8\u3002",
                30,
                999,
                true,
                StoreItemDeliveryType.VIRTUAL
        ));
        storeItemRepository.save(buildStoreItem(
                "\u5fd7\u613f\u8005\u5e06\u5e03\u888b",
                "\u5b9e\u7269\u793c\u54c1",
                "\u793e\u533a\u8054\u540d\u5e06\u5e03\u888b\uff0c\u9002\u5408\u4f5c\u4e3a\u79ef\u5206\u5151\u6362\u6fc0\u52b1\u3002",
                120,
                50,
                true,
                StoreItemDeliveryType.PHYSICAL
        ));
        storeItemRepository.save(buildStoreItem(
                "\u516c\u76ca\u6350\u8d60\u989d\u5ea6",
                "\u516c\u76ca\u6743\u76ca",
                "\u5c06\u79ef\u5206\u8f6c\u5316\u4e3a\u5e73\u53f0\u516c\u76ca\u57fa\u91d1\u6350\u8d60\u989d\u5ea6\u3002",
                80,
                200,
                true,
                StoreItemDeliveryType.VIRTUAL
        ));
    }

    private StoreItem buildStoreItem(String name,
                                     String category,
                                     String description,
                                     int pointsCost,
                                     int stock,
                                     boolean active,
                                     StoreItemDeliveryType deliveryType) {
        StoreItem storeItem = new StoreItem();
        storeItem.setName(name);
        storeItem.setCategory(category);
        storeItem.setDescription(description);
        storeItem.setPointsCost(pointsCost);
        storeItem.setStock(stock);
        storeItem.setActive(active);
        storeItem.setDeliveryType(deliveryType);
        return storeItem;
    }

    private void clearBusinessData() {
        trackPointRepository.deleteAllInBatch();
        serviceSnapshotRepository.deleteAllInBatch();
        supplementApplicationRepository.deleteAllInBatch();
        participationRecordRepository.deleteAllInBatch();
        redemptionRecordRepository.deleteAllInBatch();
        adminApplicationRepository.deleteAllInBatch();
        volunteerActivityRepository.deleteAllInBatch();
        storeItemRepository.deleteAllInBatch();
    }

    private void resetUserSummaries() {
        List<UserProfile> users = userProfileRepository.findAll();
        for (UserProfile user : users) {
            user.setTotalHours(0);
            user.setTotalPoints(0);
            user.setCreditScore(100);
            user.setBadgeName(resolveBadgeName(user.getRole()));
        }
        userProfileRepository.saveAll(users);
    }

    private String resolveBadgeName(UserRole role) {
        if (role == null) {
            return UNASSIGNED_BADGE_NAME;
        }
        if (role == UserRole.SUPER_ADMIN) {
            return SUPER_ADMIN_BADGE_NAME;
        }
        if (role == UserRole.ADMIN) {
            return ADMIN_BADGE_NAME;
        }
        return VOLUNTEER_BADGE_NAME;
    }

    private void ensureVerifiedOperator(Long operatorId, String actionName) {
        UserProfile operator = userProfileRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("操作用户不存在"));
        identityVerificationService.ensureVerified(operator, actionName);
    }
}
