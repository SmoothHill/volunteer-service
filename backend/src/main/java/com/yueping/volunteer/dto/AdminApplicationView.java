package com.yueping.volunteer.dto;

import com.yueping.volunteer.model.AdminApplication;
import com.yueping.volunteer.model.AdminApplicationStatus;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;

import java.time.LocalDateTime;

public class AdminApplicationView {

    private Long id;
    private Long userId;
    private String userName;
    private String avatarUrl;
    private UserRole currentRole;
    private String reason;
    private AdminApplicationStatus status;
    private String reviewComment;
    private Long reviewerId;
    private String reviewerName;
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;

    public static AdminApplicationView from(AdminApplication application, UserProfile user, UserProfile reviewer) {
        AdminApplicationView view = new AdminApplicationView();
        view.setId(application.getId());
        view.setUserId(application.getUserId());
        view.setReason(application.getReason());
        view.setStatus(application.getStatus());
        view.setReviewComment(application.getReviewComment());
        view.setReviewerId(application.getReviewerId());
        view.setCreatedAt(application.getCreatedAt());
        view.setReviewedAt(application.getReviewedAt());
        if (user != null) {
            view.setUserName(user.getName());
            view.setAvatarUrl(user.getAvatarUrl());
            view.setCurrentRole(user.getRole());
        }
        if (reviewer != null) {
            view.setReviewerName(reviewer.getName());
        }
        return view;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserRole getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(UserRole currentRole) {
        this.currentRole = currentRole;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AdminApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(AdminApplicationStatus status) {
        this.status = status;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
