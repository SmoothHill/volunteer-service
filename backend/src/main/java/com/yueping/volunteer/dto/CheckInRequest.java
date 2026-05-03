package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.CheckInMethod;

import javax.validation.constraints.NotNull;

public class CheckInRequest {

    @NotNull(message = "签到方式不能为空")
    private CheckInMethod method;

    private Double latitude;
    private Double longitude;
    private String checkInCode;

    public CheckInMethod getMethod() {
        return method;
    }

    public void setMethod(CheckInMethod method) {
        this.method = method;
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

    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }
}

