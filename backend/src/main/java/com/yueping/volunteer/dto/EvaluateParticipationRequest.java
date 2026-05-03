package com.yueping.volunteer.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class EvaluateParticipationRequest {

    @Min(value = 1, message = "服务评分不能低于 1 分")
    @Max(value = 5, message = "服务评分不能高于 5 分")
    private int serviceRating;

    @NotBlank(message = "评价意见不能为空")
    private String serviceComment;

    public int getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    public String getServiceComment() {
        return serviceComment;
    }

    public void setServiceComment(String serviceComment) {
        this.serviceComment = serviceComment;
    }
}
