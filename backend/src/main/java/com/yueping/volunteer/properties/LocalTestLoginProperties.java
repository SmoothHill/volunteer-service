package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.local-test-login")
public class LocalTestLoginProperties {

    private boolean enabled;
    private String volunteerOpenId = "local_test_volunteer";
    private String volunteerName = "本地测试志愿者";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVolunteerOpenId() {
        return volunteerOpenId;
    }

    public void setVolunteerOpenId(String volunteerOpenId) {
        this.volunteerOpenId = volunteerOpenId;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }
}
