package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.UN_SUPPORT_OPERATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Slf4j
@Notification(type = NotificationChannelType.EMAIL)
public class EmailChannel implements Channel {

    @Value("classpath:metadata/channel/email-input.json")
    private Resource resource;

    private final ObjectMapper objectMapper;

    public EmailChannel(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        throw new EagleEyeException(UN_SUPPORT_OPERATION);
    }

    @Override
    public List<ChannelFieldResponse> getTaskInputFields(Integer type) {
        return readMetadataFromResource(resource, objectMapper);
    }

    @Override
    public boolean send(Integer type, String config, String input) {
        return false;
    }
}