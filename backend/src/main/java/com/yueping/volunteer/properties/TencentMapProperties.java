package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.tencent-map")
public class TencentMapProperties {

    private String webServiceKey;
    private String webServiceSecretKey;
    private String apiBaseUrl = "https://apis.map.qq.com";

    public String getWebServiceKey() {
        return webServiceKey;
    }

    public void setWebServiceKey(String webServiceKey) {
        this.webServiceKey = webServiceKey;
    }

    public String getWebServiceSecretKey() {
        return webServiceSecretKey;
    }

    public void setWebServiceSecretKey(String webServiceSecretKey) {
        this.webServiceSecretKey = webServiceSecretKey;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }
}
