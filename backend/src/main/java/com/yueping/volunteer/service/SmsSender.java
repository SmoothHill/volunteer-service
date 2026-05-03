package com.yueping.volunteer.service;

public interface SmsSender {

    boolean isMock();

    void sendLoginVerificationCode(String phoneNo, String code);
}
