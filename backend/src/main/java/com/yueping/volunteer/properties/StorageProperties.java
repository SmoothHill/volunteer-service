package com.yueping.volunteer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

    private String type;
    private String rootDir;
    private String publicPrefix;
    private String cosBucket;
    private String cosRegion;
    private String cosSecretId;
    private String cosSecretKey;
    private String cosBasePath;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRootDir() {
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public String getPublicPrefix() {
        return publicPrefix;
    }

    public void setPublicPrefix(String publicPrefix) {
        this.publicPrefix = publicPrefix;
    }

    public String getCosBucket() {
        return cosBucket;
    }

    public void setCosBucket(String cosBucket) {
        this.cosBucket = cosBucket;
    }

    public String getCosRegion() {
        return cosRegion;
    }

    public void setCosRegion(String cosRegion) {
        this.cosRegion = cosRegion;
    }

    public String getCosSecretId() {
        return cosSecretId;
    }

    public void setCosSecretId(String cosSecretId) {
        this.cosSecretId = cosSecretId;
    }

    public String getCosSecretKey() {
        return cosSecretKey;
    }

    public void setCosSecretKey(String cosSecretKey) {
        this.cosSecretKey = cosSecretKey;
    }

    public String getCosBasePath() {
        return cosBasePath;
    }

    public void setCosBasePath(String cosBasePath) {
        this.cosBasePath = cosBasePath;
    }
}
