package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.login-verification")
public class LoginVerificationProperties {

    private boolean enabled = false;
    private int codeExpireMinutes = 5;
    private int resendCooldownSeconds = 60;
    private String smsMode = "prod";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCodeExpireMinutes() {
        return codeExpireMinutes;
    }

    public void setCodeExpireMinutes(int codeExpireMinutes) {
        this.codeExpireMinutes = codeExpireMinutes;
    }

    public int getResendCooldownSeconds() {
        return resendCooldownSeconds;
    }

    public void setResendCooldownSeconds(int resendCooldownSeconds) {
        this.resendCooldownSeconds = resendCooldownSeconds;
    }

    public String getSmsMode() {
        return smsMode;
    }

    public void setSmsMode(String smsMode) {
        this.smsMode = smsMode;
    }
}
