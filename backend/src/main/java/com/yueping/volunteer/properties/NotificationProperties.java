package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app.notification")
public class NotificationProperties {

    private int activityReminderMinutes = 60;
    private long reminderScanDelayMs = 600000;
    private String dispatchMode = "HYBRID";
    private Map<String, String> templateIds = new LinkedHashMap<>();
    private Map<String, String> pagePaths = new LinkedHashMap<>();
    private Map<String, List<String>> templateDataKeys = new LinkedHashMap<>();

    public int getActivityReminderMinutes() {
        return activityReminderMinutes;
    }

    public void setActivityReminderMinutes(int activityReminderMinutes) {
        this.activityReminderMinutes = activityReminderMinutes;
    }

    public long getReminderScanDelayMs() {
        return reminderScanDelayMs;
    }

    public void setReminderScanDelayMs(long reminderScanDelayMs) {
        this.reminderScanDelayMs = reminderScanDelayMs;
    }

    public String getDispatchMode() {
        return dispatchMode;
    }

    public void setDispatchMode(String dispatchMode) {
        this.dispatchMode = dispatchMode;
    }

    public Map<String, String> getTemplateIds() {
        return templateIds;
    }

    public void setTemplateIds(Map<String, String> templateIds) {
        this.templateIds = templateIds == null ? new LinkedHashMap<>() : templateIds;
    }

    public Map<String, String> getPagePaths() {
        return pagePaths;
    }

    public void setPagePaths(Map<String, String> pagePaths) {
        this.pagePaths = pagePaths == null ? new LinkedHashMap<>() : pagePaths;
    }

    public Map<String, List<String>> getTemplateDataKeys() {
        return templateDataKeys;
    }

    public void setTemplateDataKeys(Map<String, List<String>> templateDataKeys) {
        if (templateDataKeys == null) {
            this.templateDataKeys = new LinkedHashMap<>();
            return;
        }
        LinkedHashMap<String, List<String>> normalized = new LinkedHashMap<>();
        templateDataKeys.forEach((key, value) -> normalized.put(
                key,
                value == null ? new ArrayList<>() : value
        ));
        this.templateDataKeys = normalized;
    }
}
