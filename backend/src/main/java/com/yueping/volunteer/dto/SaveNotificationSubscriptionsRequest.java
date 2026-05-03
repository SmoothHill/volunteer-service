package com.yueping.volunteer.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class SaveNotificationSubscriptionsRequest {

    @Valid
    @NotEmpty(message = "订阅记录不能为空")
    private List<SaveNotificationSubscriptionItem> items;

    public List<SaveNotificationSubscriptionItem> getItems() {
        return items;
    }

    public void setItems(List<SaveNotificationSubscriptionItem> items) {
        this.items = items;
    }
}
