package com.sms.eagle.eye.backend.listener.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.listener.resolver.AwsMessage;
import com.sms.eagle.eye.backend.listener.resolver.AwsMessageGroup;
import com.sms.eagle.eye.backend.listener.resolver.MessageResolver;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@AwsMessage(group = AwsMessageGroup.ALARM)
public class AwsAlarmMessageResolver implements MessageResolver {

    private final ObjectMapper objectMapper;
    private final AlertApplicationService alertApplicationService;

    public AwsAlarmMessageResolver(ObjectMapper objectMapper,
        AlertApplicationService alertApplicationService) {
        this.objectMapper = objectMapper;
        this.alertApplicationService = alertApplicationService;
    }

    @Override
    public void resolve(String payload, String group) {
        WebHookRequest request = Try.of(() -> objectMapper.readValue(payload, WebHookRequest.class)).getOrNull();
        Assert.notNull(request, "Alarm message is not correct");
        log.info("AwsAlarmMessageResolver: {}", request);
        alertApplicationService.resolveWebHook(request);
    }
}