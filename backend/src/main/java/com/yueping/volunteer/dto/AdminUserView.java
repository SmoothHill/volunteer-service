package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;

import java.time.LocalDateTime;

public class AdminUserView {

    private Long id;
    private String name;
    private String avatarUrl;
    private UserRole role;
    private double totalHours;
    private int totalPoints;
    private LocalDateTime createdAt;

    public static AdminUserView from(UserProfile userProfile) {
        AdminUserView view = new AdminUserView();
        view.setId(userProfile.getId());
        view.setName(userProfile.getName());
        view.setAvatarUrl(userProfile.getAvatarUrl());
        view.setRole(userProfile.getRole());
        view.setTotalHours(userProfile.getTotalHours());
        view.setTotalPoints(userProfile.getTotalPoints());
        view.setCreatedAt(userProfile.getCreatedAt());
        return view;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
