package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.UN_SUPPORT_OPERATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@ExtendWith(MockitoExtension.class)
public class EmailChannelTest {

    private static final String EXCEPTION_CODE = "code";
    private static final String SUBJECT_KEY = "subject";
    private static final String RECEIVER_KEY = "receiver";
    private static final String COPY_TO_KEY = "copyTo";

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final SpringTemplateEngine engine = mock(SpringTemplateEngine.class);
    private final JavaMailSender javaMailSender = mock(JavaMailSender.class);
    private final MailProperties mailProperties = mock(MailProperties.class);

    private final EmailChannel channel = spy(new EmailChannel(objectMapper, engine, javaMailSender, mailProperties));

    /**
     * {@link EmailChannel#getConfigFields(Integer)}
     */
    @Test
    void getConfigFields_test() {
        Integer type = 1;
        // invoke and assert
        assertThatThrownBy(() -> channel.getConfigFields(type))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(UN_SUPPORT_OPERATION.getCode());
    }

    /**
     * {@link EmailChannel#getTaskInputFields(Integer)}
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
     * {@link EmailChannel#notify(NotificationEvent)}
     */
    @Test
    void notify_test() {
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
        when(engine.process(contentTemplate, context)).thenReturn(content);
        // event.getChannelInput()
        String channelInput = "channelInput";
        when(event.getChannelInput()).thenReturn(channelInput);
        // mock convertToMap()
        Map<String, Object> map = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelInput)).thenReturn(map);

        // mock subject
        String subject = "subject";
        doReturn(subject).when(channel).getValueFromMap(map, SUBJECT_KEY, String.class, true, null);
        // mock receiver
        List<String> receiver = List.of("123");
        doReturn(receiver).when(channel).getValueFromMap(map, RECEIVER_KEY, List.class, true, null);
        // mock copyTo
        List<String> copyTo = List.of("456");
        doReturn(copyTo).when(channel).getValueFromMap(map, COPY_TO_KEY, List.class, false, Collections.emptyList());
        // mock javaMailSender.createMimeMessage()
        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        // mock mailProperties.getUsername()
        String username = "username";
        when(mailProperties.getUsername()).thenReturn(username);
        // mock javaMailSender.send()
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        // invoke
        boolean result = channel.notify(event);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link EmailChannel#notify(NotificationEvent)}
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
        when(engine.process(contentTemplate, context)).thenReturn(content);
        // event.getChannelInput()
        String channelInput = "channelInput";
        when(event.getChannelInput()).thenReturn(channelInput);
        // mock convertToMap()
        Map<String, Object> map = mock(Map.class);
        when(channel.convertToMap(objectMapper, channelInput)).thenReturn(map);

        // mock subject
        String subject = "subject";
        doReturn(subject).when(channel).getValueFromMap(map, SUBJECT_KEY, String.class, true, null);
        // mock receiver
        List<String> receiver = List.of("123");
        doReturn(receiver).when(channel).getValueFromMap(map, RECEIVER_KEY, List.class, true, null);
        // mock copyTo
        List<String> copyTo = List.of("456");
        doReturn(copyTo).when(channel).getValueFromMap(map, COPY_TO_KEY, List.class, false, Collections.emptyList());
        // mock javaMailSender.createMimeMessage()
        MimeMessage message = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(message);

        // mock mailProperties.getUsername()
        String username = "username";
        when(mailProperties.getUsername()).thenReturn(username);
        // mock javaMailSender.send()
        doThrow(new RuntimeException()).when(javaMailSender).send(any(MimeMessage.class));

        // invoke
        boolean result = channel.notify(event);
        // assert
        assertThat(result).isFalse();
    }
}