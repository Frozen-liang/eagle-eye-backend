package com.sms.eagle.eye.backend.domain.service;

import com.sms.eagle.eye.backend.domain.entity.NotificationTemplateEntity;
import com.sms.eagle.eye.backend.domain.service.impl.NotificationTemplateServiceImpl;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationTemplateServiceTest {

    private final NotificationTemplateService notificationTemplateService = spy(new NotificationTemplateServiceImpl());

    private static final Integer TYPE = 1;


    @Test
    @DisplayName("Test the getByChannelAndTemplateType method in the Notification Template Service")
    public void getByChannelAndTemplateType_test() {
        doReturn(null).when(notificationTemplateService).getOne(any());
        assertThat(notificationTemplateService.getByChannelAndTemplateType(TYPE,TYPE)).isNotNull();
    }

    @Test
    @DisplayName("Test the updateTemplate method in the Notification Template Service")
    public void updateTemplate_test() {
        doReturn(Boolean.TRUE).when(notificationTemplateService).remove(any());
        NotificationTemplateRequest request = NotificationTemplateRequest.builder()
                .channelType(TYPE).templateType(TYPE).build();
        doReturn(Boolean.TRUE).when(notificationTemplateService).save(any(NotificationTemplateEntity.class));
        notificationTemplateService.updateTemplate(request);
        verify(notificationTemplateService,times(1)).remove(any());
        verify(notificationTemplateService,times(1)).save(any(NotificationTemplateEntity.class));
    }
}
