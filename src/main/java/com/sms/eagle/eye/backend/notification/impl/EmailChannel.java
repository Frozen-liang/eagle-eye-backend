package com.sms.eagle.eye.backend.notification.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.UN_SUPPORT_OPERATION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.Notification;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@Notification(type = NotificationChannelType.EMAIL)
public class EmailChannel implements Channel {

    @Value("classpath:metadata/channel/email-input.json")
    private Resource resource;

    private static final String SUBJECT_KEY = "subject";
    private static final String RECEIVER_KEY = "receiver";
    private static final String COPY_TO_KEY = "copyTo";

    private final ObjectMapper objectMapper;
    private final SpringTemplateEngine engine;
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public EmailChannel(ObjectMapper objectMapper, SpringTemplateEngine engine,
        JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.objectMapper = objectMapper;
        this.engine = engine;
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    @Override
    public List<ChannelFieldResponse> getConfigFields(Integer type) {
        throw new EagleEyeException(UN_SUPPORT_OPERATION);
    }

    @Override
    public List<ChannelFieldResponse> getTaskInputFields(Integer type) {
        return readMetadataFromResource(resource, objectMapper);
    }

    /**
     * ??? channelInput ?????? subject???receiver???copyTo.
     */
    @Override
    public boolean notify(NotificationEvent event) {
        String content = engine.process(event.getContentTemplate(),
            getTemplateContext(event.getVariableKey(), event.getVariable()));

        Map<String, Object> inputMap = convertToMap(objectMapper, event.getChannelInput());

        String subject = getValueFromMap(inputMap, SUBJECT_KEY, String.class, true, null);
        List<String> receiver = getValueFromMap(inputMap, RECEIVER_KEY, List.class, true, null);
        List<String> copyTo = getValueFromMap(inputMap, COPY_TO_KEY, List.class, false, Collections.emptyList());

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

            helper.setTo(receiver.toArray(new String[]{}));
            helper.setCc(copyTo.toArray(new String[]{}));
            helper.setText(content, true);
            helper.setSubject(subject);
            helper.setFrom(mailProperties.getUsername());
            javaMailSender.send(message);
        } catch (Exception exception) {
            log.error("Failed to send email", exception);
            return false;
        }
        return true;
    }

}