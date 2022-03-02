package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateDetailResponse;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateResponse;

public interface NotificationTemplateApplicationService {

    NotificationTemplateResponse getTemplate(Integer channelType, Integer templateType);

    boolean updateTemplate(NotificationTemplateRequest request);

    NotificationTemplateDetailResponse getTemplateWithFieldInfo(
        Integer channelType, NotificationTemplateType notificationTemplateType);
}
