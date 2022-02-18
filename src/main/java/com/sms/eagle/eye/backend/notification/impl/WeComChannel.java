package com.sms.eagle.eye.backend.notification.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Slf4j
@Notification(type = NotificationChannelType.WECOM)
public class WeComChannel implements Channel {

    @Value("classpath:metadata/channel/wecom-config.json")
    private Resource resource;

    private final ObjectMapper objectMapper;

    public WeComChannel(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        return readMetadataFromResource(resource, objectMapper);
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