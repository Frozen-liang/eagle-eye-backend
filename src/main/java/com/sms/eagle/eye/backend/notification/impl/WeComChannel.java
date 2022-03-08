package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.WECOM_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.MsgText;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.enums.MsgType;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.thymeleaf.TemplateSpec;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;

@Slf4j
@Notification(type = NotificationChannelType.WECOM)
public class WeComChannel implements Channel {

    @Value("classpath:metadata/channel/wecom-config.json")
    private Resource configResource;

    @Value("classpath:metadata/channel/wecom-input.json")
    private Resource inputResource;

    private static final String CORP_ID = "corp_id";
    private static final String CORP_SECRET = "corp_secret";
    private static final String AGENT_ID = "agent_id";
    private static final String TO_USER = "to_user";
    private static final String TO_USER_SEPARATOR = "|";

    private final ObjectMapper objectMapper;
    private final SpringTemplateEngine engine;
    private final WeComManager weComManager;

    public WeComChannel(ObjectMapper objectMapper, SpringTemplateEngine engine,
        WeComManager weComManager) {
        this.objectMapper = objectMapper;
        this.engine = engine;
        this.weComManager = weComManager;
    }

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        return readMetadataFromResource(configResource, objectMapper);
    }

    @Override
    public List<ChannelFieldResponse> getTaskInputFields(Integer type) {
        return readMetadataFromResource(inputResource, objectMapper);
    }

    @Override
    public boolean notify(NotificationEvent event) {
        TemplateSpec templateSpec = new TemplateSpec(event.getContentTemplate(), TemplateMode.TEXT);
        String content = engine.process(templateSpec, getTemplateContext(event.getVariableKey(), event.getVariable()));

        Map<String, Object> configMap = convertToMap(objectMapper, event.getChannelConfig());
        Map<String, Object> inputMap = convertToMap(objectMapper, event.getChannelInput());

        String corpId = getValueFromMap(configMap, CORP_ID, String.class, true, null);
        String corpSecret = getValueFromMap(configMap, CORP_SECRET, String.class, true, null);
        String agentId = getValueFromMap(configMap, AGENT_ID, String.class, true, null);
        List<String> toUser = getValueFromMap(inputMap, TO_USER, List.class, true, null);

        WeComPropertiesContextHolder.setProperties(WeComProperties.builder()
            .corpId(corpId).corpSecret(corpSecret).build());

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
            .agentId(agentId)
            .msgType(MsgType.TEXT)
            .text(MsgText.builder().content(content).build())
            .toUser(StringUtils.join(toUser, TO_USER_SEPARATOR))
            .build();
        try {
            weComManager.messageService().sendMessage(sendMessageRequest, agentId);
        } catch (Exception exception) {
            throw new EagleEyeException(WECOM_ERROR, exception.getMessage());
        }
        return true;
    }
}