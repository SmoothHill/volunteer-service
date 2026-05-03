package com.yueping.volunteer.dto;

import javax.validation.constraints.NotBlank;

public class SaveNotificationSubscriptionItem {

    @NotBlank(message = "消息类型不能为空")
    private String notificationType;

    @NotBlank(message = "模板编号不能为空")
    private String templateCode;

    @NotBlank(message = "订阅结果不能为空")
    private String acceptStatus;

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(String acceptStatus) {
        this.acceptStatus = acceptStatus;
    }
}
