package com.sms.eagle.eye.backend.listener.resolver.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import org.junit.jupiter.api.Test;

public class AwsAlarmMessageResolverTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final AlertApplicationService alertApplicationService = mock(AlertApplicationService.class);
    private final AwsAlarmMessageResolver resolver = spy(
        new AwsAlarmMessageResolver(objectMapper, alertApplicationService));

    /**
     * {@link AwsAlarmMessageResolver#resolve(String, String)}
     *
     * <p>情形1：
     */
    @Test
    void resolve_test_1() throws Exception {
        String payload = "payload";
        String group = "group";
        // mock objectMapper.readValue()
        WebHookRequest request = mock(WebHookRequest.class);
        when(objectMapper.readValue(payload, WebHookRequest.class)).thenReturn(request);
        // mock alertApplicationService.resolveWebHook()
        doReturn(true).when(alertApplicationService).resolveWebHook(request);
        // invoke
        resolver.resolve(payload, group);
        // verify
        verify(alertApplicationService).resolveWebHook(request);
    }
}