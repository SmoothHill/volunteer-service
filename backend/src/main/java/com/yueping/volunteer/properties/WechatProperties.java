package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.wechat")
public class WechatProperties {

    private String appId;
    private String appSecret;
    private String code2sessionUrl;
    private String accessTokenUrl;
    private String subscribeMessageSendUrl;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getCode2sessionUrl() {
        return code2sessionUrl;
    }

    public void setCode2sessionUrl(String code2sessionUrl) {
        this.code2sessionUrl = code2sessionUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public void setAccessTokenUrl(String accessTokenUrl) {
        this.accessTokenUrl = accessTokenUrl;
    }

    public String getSubscribeMessageSendUrl() {
        return subscribeMessageSendUrl;
    }

    public void setSubscribeMessageSendUrl(String subscribeMessageSendUrl) {
        this.subscribeMessageSendUrl = subscribeMessageSendUrl;
    }
}
