package com.yueping.volunteer.dto;

public class WechatLoginStartResponse {

    private boolean needPhoneVerification;
    private String loginTicket;
    private AuthLoginResponse loginResult;

    public static WechatLoginStartResponse directLogin(AuthLoginResponse loginResult) {
        WechatLoginStartResponse response = new WechatLoginStartResponse();
        response.setNeedPhoneVerification(false);
        response.setLoginResult(loginResult);
        return response;
    }

    public static WechatLoginStartResponse pendingPhoneVerification(String loginTicket) {
        WechatLoginStartResponse response = new WechatLoginStartResponse();
        response.setNeedPhoneVerification(true);
        response.setLoginTicket(loginTicket);
        return response;
    }

    public boolean isNeedPhoneVerification() {
        return needPhoneVerification;
    }

    public void setNeedPhoneVerification(boolean needPhoneVerification) {
        this.needPhoneVerification = needPhoneVerification;
    }

    public String getLoginTicket() {
        return loginTicket;
    }

    public void setLoginTicket(String loginTicket) {
        this.loginTicket = loginTicket;
    }

    public AuthLoginResponse getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(AuthLoginResponse loginResult) {
        this.loginResult = loginResult;
    }
}
