package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.UserProfile;

public class AuthLoginResponse {

    private String token;
    private UserProfile profile;

    public AuthLoginResponse() {
    }

    public AuthLoginResponse(String token, UserProfile profile) {
        this.token = token;
        this.profile = profile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }
}

