package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.NotificationTemplateEntity;
import com.sms.eagle.eye.backend.domain.mapper.NotificationTemplateMapper;
import com.sms.eagle.eye.backend.domain.service.NotificationTemplateService;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@DomainServiceAdvice
public class NotificationTemplateServiceImpl extends ServiceImpl<NotificationTemplateMapper, NotificationTemplateEntity>
    implements NotificationTemplateService {

    /**
     * channelType 与 templateType 联合唯一.
     */
    @Override
    public Optional<NotificationTemplateEntity> getByChannelAndTemplateType(Integer channelType, Integer templateType) {
        return Optional.of(getOne(Wrappers.<NotificationTemplateEntity>lambdaQuery()
            .eq(NotificationTemplateEntity::getChannelType, channelType)
            .eq(NotificationTemplateEntity::getTemplateType, templateType)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTemplate(NotificationTemplateRequest request) {
        remove(Wrappers.<NotificationTemplateEntity>lambdaQuery()
            .eq(NotificationTemplateEntity::getChannelType, request.getChannelType())
            .eq(NotificationTemplateEntity::getTemplateType, request.getTemplateType()));
        save(NotificationTemplateEntity.builder()
            .channelType(request.getChannelType())
            .templateType(request.getTemplateType())
            .template(request.getTemplate())
            .build());
    }
}




