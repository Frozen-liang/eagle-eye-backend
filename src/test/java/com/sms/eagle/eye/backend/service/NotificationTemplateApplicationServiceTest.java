package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.domain.service.NotificationTemplateService;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import com.sms.eagle.eye.backend.service.impl.NotificationTemplateApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class NotificationTemplateApplicationServiceTest {

    private final NotificationTemplateService notificationTemplateService = mock(NotificationTemplateService.class);
    private final NotificationTemplateApplicationService notificationTemplateApplicationService =
            new NotificationTemplateApplicationServiceImpl(notificationTemplateService);
    private final NotificationTemplateRequest request = mock(NotificationTemplateRequest.class);

    private static final Integer TYPE = 1;

    @Test
    @DisplayName("Test the updateTemplate method in the Notification Template Application Service")
    public void getTemplate_test() {
        when(notificationTemplateService.getByChannelAndTemplateType(TYPE, TYPE)).thenReturn(Optional.empty());
        assertThat(notificationTemplateApplicationService.getTemplate(TYPE, TYPE)).isNotNull();
    }

    @Test
    @DisplayName("Test the updateTemplate method in the Notification Template Application Service")
    public void updateTemplate_test() {
        doNothing().when(notificationTemplateService).updateTemplate(request);
        assertThat(notificationTemplateApplicationService.updateTemplate(request)).isTrue();
    }

    @Test
    @DisplayName("Test the getTemplateWithFieldInfo method in the Notification Template Application Service")
    public void getTemplateWithFieldInfo_test() {
        NotificationTemplateType templateType = mock(NotificationTemplateType.class);
        when(notificationTemplateService.getByChannelAndTemplateType(TYPE, TYPE)).thenReturn(Optional.empty());
        when(templateType.getFieldList()).thenReturn(Collections.emptyList());
        assertThat(notificationTemplateApplicationService.getTemplateWithFieldInfo(TYPE, templateType)).isNotNull();
    }
}
