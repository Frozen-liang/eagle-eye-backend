package com.sms.eagle.eye.backend.listener.resolver.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.event.TaskInvokeFailedEvent;
import com.sms.eagle.eye.backend.event.TaskInvokeSuccessEvent;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

public class AwsErrorMessageResolverTest {

    private static final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private static final ApplicationContext applicationContext = mock(ApplicationContext.class);
    private static final AwsErrorMessageResolver resolver = spy(new AwsErrorMessageResolver(objectMapper));

    @BeforeAll
    public static void init() {
        resolver.setApplicationContext(applicationContext);
    }

    /**
     * {@link AwsErrorMessageResolver#resolve(String, String)}
     *
     * <p>
     */
    @Test
    void resolve_test_1() throws JsonProcessingException {
        String payload = "payload";
        String group = "group";
        // mock objectMapper.readValue
        LambdaInvokeResult result = mock(LambdaInvokeResult.class);
        when(objectMapper.readValue(payload, LambdaInvokeResult.class)).thenReturn(result);
        // mock result.getSuccess()
        when(result.getSuccess()).thenReturn(Boolean.TRUE);
        // mock applicationContext.publishEvent
        doNothing().when(applicationContext).publishEvent(any(TaskInvokeSuccessEvent.class));
        // invoke
        resolver.resolve(payload, group);
        verify(applicationContext).publishEvent(any(TaskInvokeSuccessEvent.class));
    }

    /**
     * {@link AwsErrorMessageResolver#resolve(String, String)}
     *
     * <p>
     */
    @Test
    void resolve_test_2() throws JsonProcessingException {
        String payload = "payload";
        String group = "group";
        // mock objectMapper.readValue
        LambdaInvokeResult result = mock(LambdaInvokeResult.class);
        when(objectMapper.readValue(payload, LambdaInvokeResult.class)).thenReturn(result);
        // mock result.getSuccess()
        when(result.getSuccess()).thenReturn(Boolean.FALSE);
        // mock applicationContext.publishEvent
        doNothing().when(applicationContext).publishEvent(any(TaskInvokeFailedEvent.class));
        // invoke
        resolver.resolve(payload, group);
        verify(applicationContext).publishEvent(any(TaskInvokeFailedEvent.class));
    }
}