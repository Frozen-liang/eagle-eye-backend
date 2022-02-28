package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.NotificationTemplateEntity;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import java.util.Optional;

public interface NotificationTemplateService extends IService<NotificationTemplateEntity> {

    Optional<NotificationTemplateEntity> getByChannelAndTemplateType(Integer channelType, Integer templateType);

    void updateTemplate(NotificationTemplateRequest request);
}
