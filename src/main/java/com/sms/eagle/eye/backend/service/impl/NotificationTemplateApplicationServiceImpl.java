package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.domain.entity.NotificationTemplateEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationTemplateService;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateDetailResponse;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateResponse;
import com.sms.eagle.eye.backend.service.NotificationTemplateApplicationService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class NotificationTemplateApplicationServiceImpl implements NotificationTemplateApplicationService {

    private static final String DEFAULT_TEMPLATE = "";

    private final NotificationTemplateService notificationTemplateService;

    public NotificationTemplateApplicationServiceImpl(
        NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

    @Override
    public NotificationTemplateResponse getTemplate(Integer channelType, Integer templateType) {
        return NotificationTemplateResponse.builder()
            .template(notificationTemplateService.getByChannelAndTemplateType(channelType, templateType)
                .map(NotificationTemplateEntity::getTemplate).orElse(null))
            .build();
    }

    @Override
    public boolean updateTemplate(NotificationTemplateRequest request) {
        notificationTemplateService.updateTemplate(request);
        return true;
    }

    @Override
    public NotificationTemplateDetailResponse getTemplateWithFieldInfo(Integer channelType,
        NotificationTemplateType notificationTemplateType) {
        Optional<NotificationTemplateEntity> entityOptional = notificationTemplateService
            .getByChannelAndTemplateType(channelType, notificationTemplateType.getValue());
        return NotificationTemplateDetailResponse.builder()
            .template(entityOptional.map(NotificationTemplateEntity::getTemplate).orElse(DEFAULT_TEMPLATE))
            .fieldList(notificationTemplateType.getFieldList())
            .build();
    }
}