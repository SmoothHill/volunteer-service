package com.yueping.volunteer.dto;

import javax.validation.constraints.NotNull;

public class ReviewAdminApplicationRequest {

    @NotNull(message = "请指定审核结果")
    private Boolean approved;

    private String reviewComment;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
