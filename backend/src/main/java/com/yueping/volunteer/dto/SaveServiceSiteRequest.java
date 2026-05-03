package com.yueping.volunteer.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SaveServiceSiteRequest {

    @NotBlank(message = "站点名称不能为空")
    private String name;

    @NotBlank(message = "所属社区不能为空")
    private String communityName;

    @NotBlank(message = "所属街道不能为空")
    private String streetName;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    @NotNull(message = "纬度不能为空")
    private Double latitude;

    @NotNull(message = "经度不能为空")
    private Double longitude;

    @NotNull(message = "推荐签到半径不能为空")
    @Min(value = 50, message = "推荐签到半径不能小于 50 米")
    @Max(value = 2000, message = "推荐签到半径不能大于 2000 米")
    private Integer recommendedRadiusMeters;

    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRecommendedRadiusMeters() {
        return recommendedRadiusMeters;
    }

    public void setRecommendedRadiusMeters(Integer recommendedRadiusMeters) {
        this.recommendedRadiusMeters = recommendedRadiusMeters;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
