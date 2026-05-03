package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SupplementReviewRequest {

    @NotNull(message = "审核结果不能为空")
    private Boolean approved;

    @NotBlank(message = "审核意见不能为空")
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

