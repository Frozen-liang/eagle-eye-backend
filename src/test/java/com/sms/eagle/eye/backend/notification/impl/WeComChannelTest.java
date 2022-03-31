package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.WECOM_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.service.MessageService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

public class WeComChannelTest {

    private static final String CORP_ID = "corp_id";
    private static final String CORP_SECRET = "corp_secret";
    private static final String AGENT_ID = "agent_id";
    private static final String TO_USER = "to_user";
    private static final String EXCEPTION_CODE = "code";

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final SpringTemplateEngine engine = mock(SpringTemplateEngine.class);
    private final WeComManager weComManager = mock(WeComManager.class);

    private final WeComChannel channel = spy(new WeComChannel(objectMapper, engine, weComManager));

    /**
     * {@link WeComChannel#getConfigFields(Integer)}
     */
    @Test
    void getConfigFields_test() {
        Integer type = 1;
        // mock readMetadataFromResource()
        List<ChannelFieldResponse> channelFieldResponses = mock(List.class);
        doReturn(channelFieldResponses).when(channel)
            .readMetadataFromResource(any(), any(ObjectMapper.class));
        // invoke
        List<ChannelFieldResponse> result = channel.getConfigFields(type);
        // assert
        assertThat(result).isEqualTo(channelFieldResponses);
    }

    /**
     * {@link WeComChannel#getTaskInputFields(Integer)}
     */
    @Test
    void getTaskInputFields_test() {
        Integer type = 1;
        // mock readMetadataFromResource()
        List<ChannelFieldResponse> channelFieldResponses = mock(List.class);
        doReturn(channelFieldResponses).when(channel)
            .readMetadataFromResource(any(), any(ObjectMapper.class));
        // invoke
        List<ChannelFieldResponse> result = channel.getTaskInputFields(type);
        // assert
        assertThat(result).isEqualTo(channelFieldResponses);
    }

    /**
     * {@link WeComChannel#notify(NotificationEvent)}
     */
    @Test
    void notify_test_1() {
        NotificationEvent event = mock(NotificationEvent.class);
        // mock event.getContentTemplate()
        String contentTemplate = "contentTemplate";
        when(event.getContentTemplate()).thenReturn(contentTemplate);
        // mock event.getVariableKey()
        String variableKey = "variableKey";
        when(event.getVariableKey()).thenReturn(variableKey);
        // mock event.getVariable()
        Object variable = 12;
        when(event.getVariable()).thenReturn(variable);
        // mock getTemplateContext()
        Context context = mock(Context.class);
        when(channel.getTemplateContext(variableKey, variable)).thenReturn(context);
        // mock engine.process
        String content = "content";
        when(engine.process(any(TemplateSpec.class), any(Context.class))).thenReturn(content);
        // mock event.getChannelConfig()
        String channelConfig = "channelConfig";
        when(event.getChannelConfig()).thenReturn(channelConfig);
        // mock configMap
        Map<String, Object> configMap = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelConfig)).thenReturn(configMap);
        // mock event.getChannelInput()
        String channelInput = "channelInput";
        when(event.getChannelInput()).thenReturn(channelInput);
        // mock inputMap
        Map<String, Object> inputMap = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelInput)).thenReturn(inputMap);
        // mock corpId
        String corpId = "corpId";
        doReturn(corpId).when(channel).getValueFromMap(configMap, CORP_ID, String.class, true, null);
        // mock corpSecret
        String corpSecret = "corpSecret";
        doReturn(corpSecret).when(channel).getValueFromMap(configMap, CORP_SECRET, String.class, true, null);
        // mock agentId
        String agentId = "agentId";
        doReturn(agentId).when(channel).getValueFromMap(configMap, AGENT_ID, String.class, true, null);
        // mock toUser
        List<String> toUser = List.of("123", "456");
        doReturn(toUser).when(channel).getValueFromMap(inputMap, TO_USER, List.class, true, null);
        // mock messageService()
        MessageService messageService = mock(MessageService.class);
        when(weComManager.messageService()).thenReturn(messageService);
        // invoke
        boolean result = channel.notify(event);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link WeComChannel#notify(NotificationEvent)}
     */
    @Test
    void notify_test_2() {
        NotificationEvent event = mock(NotificationEvent.class);
        // mock event.getContentTemplate()
        String contentTemplate = "contentTemplate";
        when(event.getContentTemplate()).thenReturn(contentTemplate);
        // mock event.getVariableKey()
        String variableKey = "variableKey";
        when(event.getVariableKey()).thenReturn(variableKey);
        // mock event.getVariable()
        Object variable = 12;
        when(event.getVariable()).thenReturn(variable);
        // mock getTemplateContext()
        Context context = mock(Context.class);
        when(channel.getTemplateContext(variableKey, variable)).thenReturn(context);
        // mock engine.process
        String content = "content";
        when(engine.process(any(TemplateSpec.class), any(Context.class))).thenReturn(content);
        // mock event.getChannelConfig()
        String channelConfig = "channelConfig";
        when(event.getChannelConfig()).thenReturn(channelConfig);
        // mock configMap
        Map<String, Object> configMap = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelConfig)).thenReturn(configMap);
        // mock event.getChannelInput()
        String channelInput = "channelInput";
        when(event.getChannelInput()).thenReturn(channelInput);
        // mock inputMap
        Map<String, Object> inputMap = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelInput)).thenReturn(inputMap);
        // mock corpId
        String corpId = "corpId";
        doReturn(corpId).when(channel).getValueFromMap(configMap, CORP_ID, String.class, true, null);
        // mock corpSecret
        String corpSecret = "corpSecret";
        doReturn(corpSecret).when(channel).getValueFromMap(configMap, CORP_SECRET, String.class, true, null);
        // mock agentId
        String agentId = "agentId";
        doReturn(agentId).when(channel).getValueFromMap(configMap, AGENT_ID, String.class, true, null);
        // mock toUser
        List<String> toUser = List.of("123", "456");
        doReturn(toUser).when(channel).getValueFromMap(inputMap, TO_USER, List.class, true, null);
        // mock messageService()
        MessageService messageService = mock(MessageService.class);
        when(weComManager.messageService()).thenReturn(messageService);
        // mock throw
        doThrow(new RuntimeException()).when(messageService).sendMessage(any(SendMessageRequest.class), anyString());
        // invoke and assert
        assertThatThrownBy(() -> channel.notify(event))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(WECOM_ERROR.getCode());
    }
}