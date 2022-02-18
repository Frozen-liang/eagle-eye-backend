package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;

public interface NotificationChannelService extends IService<NotificationChannelEntity> {

    IPage<NotificationChannelEntity> getPage(NotificationChannelQueryRequest request);

    NotificationChannelEntity getEntityById(Long channelId);

    void saveFromRequest(NotificationChannelRequest request);

    void updateFromRequest(NotificationChannelRequest request);

    void deleteEntity(Long id);

}
