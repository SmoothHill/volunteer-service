package com.yueping.volunteer.service;

import com.yueping.volunteer.dto.UpdateSystemSettingsRequest;
import com.yueping.volunteer.model.SystemSetting;
import com.yueping.volunteer.model.SystemSettingsView;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.repository.SystemSettingRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SystemSettingService {

    private static final String KEY_SYSTEM_NAME = "system.name";
    private static final String KEY_DEFAULT_GEOFENCE_RADIUS = "activity.defaultGeofenceRadiusMeters";
    private static final String KEY_MIN_TRACK_POINTS = "track.minPoints";
    private static final String KEY_MIN_SNAPSHOT_COUNT = "snapshot.minCount";
    private static final String KEY_MIN_SERVICE_HOURS = "activity.minServiceHours";
    private static final String KEY_FREQUENT_SUPPLEMENT_LIMIT = "anomaly.frequentSupplementLimit30Days";
    private static final String KEY_CERTIFICATE_TITLE = "certificate.title";
    private static final String KEY_SUPPLEMENT_APPROVE_COMMENT = "supplement.approveComment";
    private static final String KEY_ADMIN_WEB_URL = "admin.webUrl";

    private static final String DEFAULT_SYSTEM_NAME = "\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0";
    private static final int DEFAULT_GEOFENCE_RADIUS = 300;
    private static final int DEFAULT_MIN_TRACK_POINTS = 3;
    private static final int DEFAULT_MIN_SNAPSHOT_COUNT = 1;
    private static final double DEFAULT_MIN_SERVICE_HOURS = 0.5D;
    private static final int DEFAULT_FREQUENT_SUPPLEMENT_LIMIT = 3;
    private static final String DEFAULT_CERTIFICATE_TITLE = "\u5fd7\u613f\u670d\u52a1\u7535\u5b50\u8bc1\u4e66";
    private static final String DEFAULT_SUPPLEMENT_APPROVE_COMMENT = "\u5ba1\u6838\u901a\u8fc7\uff0c\u5df2\u5b8c\u6210\u8865\u7b7e\u3002";
    private static final String DEFAULT_ADMIN_WEB_URL = "";

    private final SystemSettingRepository systemSettingRepository;
    private final UserProfileRepository userProfileRepository;
    private final IdentityVerificationService identityVerificationService;
    private final AdminAuditLogService adminAuditLogService;

    public SystemSettingService(SystemSettingRepository systemSettingRepository,
                                UserProfileRepository userProfileRepository,
                                IdentityVerificationService identityVerificationService,
                                AdminAuditLogService adminAuditLogService) {
        this.systemSettingRepository = systemSettingRepository;
        this.userProfileRepository = userProfileRepository;
        this.identityVerificationService = identityVerificationService;
        this.adminAuditLogService = adminAuditLogService;
    }

    @Transactional(readOnly = true)
    public SystemSettingsView getSettings() {
        SystemSettingsView view = new SystemSettingsView();
        view.setSystemName(getSystemName());
        view.setDefaultGeofenceRadiusMeters(getDefaultGeofenceRadiusMeters());
        view.setMinTrackPoints(getMinTrackPoints());
        view.setMinSnapshotCount(getMinSnapshotCount());
        view.setMinServiceHours(getMinServiceHours());
        view.setFrequentSupplementLimit30Days(getFrequentSupplementLimit30Days());
        view.setCertificateTitle(getCertificateTitle());
        view.setSupplementApproveComment(getSupplementApproveComment());
        view.setAdminWebUrl(getAdminWebUrl());
        return view;
    }

    public SystemSettingsView updateSettings(UpdateSystemSettingsRequest request, Long operatorId) {
        ensureVerifiedOperator(operatorId, "\u66f4\u65b0\u7cfb\u7edf\u8bbe\u7f6e");
        save(KEY_SYSTEM_NAME, normalizeText(request.getSystemName(), DEFAULT_SYSTEM_NAME));
        save(KEY_DEFAULT_GEOFENCE_RADIUS, String.valueOf(request.getDefaultGeofenceRadiusMeters()));
        save(KEY_MIN_TRACK_POINTS, String.valueOf(request.getMinTrackPoints()));
        save(KEY_MIN_SNAPSHOT_COUNT, String.valueOf(request.getMinSnapshotCount()));
        save(KEY_MIN_SERVICE_HOURS, String.valueOf(request.getMinServiceHours()));
        save(KEY_FREQUENT_SUPPLEMENT_LIMIT, String.valueOf(request.getFrequentSupplementLimit30Days()));
        save(KEY_CERTIFICATE_TITLE, normalizeText(request.getCertificateTitle(), DEFAULT_CERTIFICATE_TITLE));
        save(KEY_SUPPLEMENT_APPROVE_COMMENT, normalizeText(request.getSupplementApproveComment(), DEFAULT_SUPPLEMENT_APPROVE_COMMENT));
        save(KEY_ADMIN_WEB_URL, normalizeUrl(request.getAdminWebUrl()));

        SystemSettingsView view = getSettings();
        adminAuditLogService.log(
                operatorId,
                "SYSTEM_SETTINGS_UPDATED",
                "SYSTEM_SETTINGS",
                "GLOBAL",
                "\u66f4\u65b0\u7cfb\u7edf\u8bbe\u7f6e\uff1a\u7cfb\u7edf\u540d\u79f0 " + view.getSystemName()
                        + "\uff0c\u9ed8\u8ba4\u56f4\u680f\u534a\u5f84 " + view.getDefaultGeofenceRadiusMeters()
                        + "\uff0c\u6700\u5c11\u8f68\u8ff9\u70b9\u6570 " + view.getMinTrackPoints()
                        + "\uff0c\u6700\u5c11\u5feb\u7167\u6570\u91cf " + view.getMinSnapshotCount()
                        + "\uff0c\u6700\u77ed\u670d\u52a1\u65f6\u957f " + view.getMinServiceHours()
                        + "\uff0c30\u5929\u8865\u7b7e\u9608\u503c " + view.getFrequentSupplementLimit30Days()
        );
        return view;
    }

    @Transactional(readOnly = true)
    public String getSystemName() {
        return getString(KEY_SYSTEM_NAME, DEFAULT_SYSTEM_NAME);
    }

    @Transactional(readOnly = true)
    public int getDefaultGeofenceRadiusMeters() {
        return getInt(KEY_DEFAULT_GEOFENCE_RADIUS, DEFAULT_GEOFENCE_RADIUS);
    }

    @Transactional(readOnly = true)
    public int getMinTrackPoints() {
        return getInt(KEY_MIN_TRACK_POINTS, DEFAULT_MIN_TRACK_POINTS);
    }

    @Transactional(readOnly = true)
    public int getMinSnapshotCount() {
        return getInt(KEY_MIN_SNAPSHOT_COUNT, DEFAULT_MIN_SNAPSHOT_COUNT);
    }

    @Transactional(readOnly = true)
    public double getMinServiceHours() {
        return getDouble(KEY_MIN_SERVICE_HOURS, DEFAULT_MIN_SERVICE_HOURS);
    }

    @Transactional(readOnly = true)
    public int getFrequentSupplementLimit30Days() {
        return getInt(KEY_FREQUENT_SUPPLEMENT_LIMIT, DEFAULT_FREQUENT_SUPPLEMENT_LIMIT);
    }

    @Transactional(readOnly = true)
    public String getCertificateTitle() {
        return getString(KEY_CERTIFICATE_TITLE, DEFAULT_CERTIFICATE_TITLE);
    }

    @Transactional(readOnly = true)
    public String getSupplementApproveComment() {
        return getString(KEY_SUPPLEMENT_APPROVE_COMMENT, DEFAULT_SUPPLEMENT_APPROVE_COMMENT);
    }

    @Transactional(readOnly = true)
    public String getAdminWebUrl() {
        return getString(KEY_ADMIN_WEB_URL, DEFAULT_ADMIN_WEB_URL);
    }

    private void save(String key, String value) {
        SystemSetting setting = systemSettingRepository.findById(key).orElseGet(SystemSetting::new);
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setUpdatedAt(LocalDateTime.now());
        systemSettingRepository.save(setting);
    }

    private String getString(String key, String defaultValue) {
        return systemSettingRepository.findById(key)
                .map(SystemSetting::getSettingValue)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .orElse(defaultValue);
    }

    private int getInt(String key, int defaultValue) {
        String value = getString(key, String.valueOf(defaultValue));
        try {
            int parsed = Integer.parseInt(value);
            return parsed >= 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private double getDouble(String key, double defaultValue) {
        String value = getString(key, String.valueOf(defaultValue));
        try {
            double parsed = Double.parseDouble(value);
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private String normalizeText(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? defaultValue : trimmed;
    }

    private String normalizeUrl(String value) {
        if (value == null) {
            return DEFAULT_ADMIN_WEB_URL;
        }
        return value.trim();
    }

    private UserProfile ensureVerifiedOperator(Long operatorId, String actionName) {
        UserProfile operator = userProfileRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("\u64cd\u4f5c\u4eba\u4e0d\u5b58\u5728"));
        identityVerificationService.ensureVerified(operator, actionName);
        return operator;
    }
}
