package com.sms.eagle.eye.backend.notification.impl;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.List;

@Notification(type = NotificationChannelType.WEBHOOK)
public class WebhookChannel implements Channel {

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        return null;
    }

    @Override
    public List<ChannelFieldResponse> getTaskInputFields(Integer type) {
        return null;
    }

    @Override
    public boolean send(Integer type, String config, String input) {
        return false;
    }
}