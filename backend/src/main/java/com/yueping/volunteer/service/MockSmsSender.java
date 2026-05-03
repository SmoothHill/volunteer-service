package com.yueping.volunteer.service;

import com.yueping.volunteer.properties.LoginVerificationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MockSmsSender implements SmsSender {

    private static final Logger log = LoggerFactory.getLogger(MockSmsSender.class);

    private final LoginVerificationProperties loginVerificationProperties;

    public MockSmsSender(LoginVerificationProperties loginVerificationProperties) {
        this.loginVerificationProperties = loginVerificationProperties;
    }

    @Override
    public boolean isMock() {
        return "mock".equalsIgnoreCase(normalizeMode());
    }

    @Override
    public void sendLoginVerificationCode(String phoneNo, String code) {
        if (!isMock()) {
            throw new IllegalStateException("Mock SMS sender is disabled");
        }
        log.warn("Mock SMS code generated: phoneNo={}, code={}", phoneNo, code);
    }

    private String normalizeMode() {
        String mode = loginVerificationProperties.getSmsMode();
        return StringUtils.hasText(mode) ? mode.trim() : "prod";
    }
}
