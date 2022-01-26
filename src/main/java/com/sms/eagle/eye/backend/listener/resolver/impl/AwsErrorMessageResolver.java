package com.sms.eagle.eye.backend.listener.resolver.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.event.TaskInvokeFailedEvent;
import com.sms.eagle.eye.backend.event.TaskInvokeSuccessEvent;
import com.sms.eagle.eye.backend.listener.resolver.AwsMessage;
import com.sms.eagle.eye.backend.listener.resolver.AwsMessageGroup;
import com.sms.eagle.eye.backend.listener.resolver.MessageResolver;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import io.vavr.control.Try;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

@Slf4j
@AwsMessage(group = AwsMessageGroup.INVOKE_ERROR)
public class AwsErrorMessageResolver implements MessageResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    public AwsErrorMessageResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void resolve(String payload, String group) {
        LambdaInvokeResult result = Try.of(() -> objectMapper.readValue(payload, LambdaInvokeResult.class)).getOrNull();
        Assert.notNull(result, "Invoke error message is not correct");
        log.info("AwsErrorMessageResolver: {}", result);
        if (Objects.equals(result.getSuccess(), Boolean.TRUE)) {
            applicationContext.publishEvent(TaskInvokeSuccessEvent.builder()
                .taskId(result.getTaskId())
                .mappingId(result.getMappingId())
                .build());

        } else {
            applicationContext.publishEvent(TaskInvokeFailedEvent.builder()
                .taskId(result.getTaskId())
                .errMsg(result.getErrorMsg())
                .build());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}