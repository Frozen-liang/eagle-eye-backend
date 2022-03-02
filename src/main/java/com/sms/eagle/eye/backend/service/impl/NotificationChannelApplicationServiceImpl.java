package com.sms.eagle.eye.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sms.eagle.eye.backend.convert.NotificationChannelConverter;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationChannelService;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.impl.ChannelProxy;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import com.sms.eagle.eye.backend.service.NotificationChannelApplicationService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationChannelApplicationServiceImpl implements NotificationChannelApplicationService {

    private static final String DEFAULT_VALUE = "{}";

    private final Channel channel;
    private final NotificationChannelConverter converter;
    private final ConfigMetadataResolver configMetadataResolver;
    private final NotificationChannelService notificationChannelService;

    public NotificationChannelApplicationServiceImpl(
        @Qualifier(ChannelProxy.CHANNEL_PROXY) Channel channel, NotificationChannelConverter converter,
        ConfigMetadataResolver configMetadataResolver,
        NotificationChannelService notificationChannelService) {
        this.channel = channel;
        this.configMetadataResolver = configMetadataResolver;
        this.notificationChannelService = notificationChannelService;
        this.converter = converter;
    }

    @Override
    public List<ChannelListResponse> getList() {
        return notificationChannelService.getList();
    }

    @Override
    public CustomPage<ChannelPageResponse> getPage(NotificationChannelQueryRequest request) {
        IPage<ChannelPageResponse> page = notificationChannelService.getPage(request).convert(converter::toResponse);
        return new CustomPage<>(page);
    }

    @Override
    public List<ChannelFieldResponse> getConfigFieldsByType(Integer type) {
        return channel.getConfigFields(type);
    }

    @Override
    public List<ChannelFieldResponse> getInputFieldsByType(Integer type) {
        return channel.getTaskInputFields(type);
    }

    @Override
    public ChannelDetailResponse getByChannelId(Long channelId) {
        NotificationChannelEntity entity = notificationChannelService.getEntityById(channelId);
        List<ChannelFieldResponse> fields = getConfigFieldsByType(entity.getType());
        Map<String, String> map = configMetadataResolver.convertConfigToMap(entity.getConfig());
        List<ChannelFieldWithValueResponse> config = fields.stream().map(field -> {
            ConfigMetadata metadata = ConfigMetadata.builder().build();
            BeanUtils.copyProperties(field, metadata);
            String value = configMetadataResolver.decryptToFrontendValue(metadata, map);
            ChannelFieldWithValueResponse response = ChannelFieldWithValueResponse.builder().build();
            BeanUtils.copyProperties(field, response);
            response.setValue(value);
            return response;
        }).collect(Collectors.toList());
        return ChannelDetailResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .config(config)
            .build();
    }

    @Override
    public boolean addChannel(NotificationChannelRequest request) {
        List<ConfigMetadata> metadataList = getConfigFieldsByType(request.getType()).stream()
            .map(field -> {
                ConfigMetadata metadata = ConfigMetadata.builder().build();
                BeanUtils.copyProperties(field, metadata);
                return metadata;
            })
            .collect(Collectors.toList());
        String config = configMetadataResolver.checkAndEncrypt(metadataList, request.getConfig(), DEFAULT_VALUE);
        request.setConfig(config);
        notificationChannelService.saveFromRequest(request);
        return true;
    }

    @Override
    public boolean updateChannel(NotificationChannelRequest request) {
        NotificationChannelEntity entity = notificationChannelService.getEntityById(request.getId());
        List<ConfigMetadata> metadataList = getConfigFieldsByType(entity.getType()).stream()
            .map(field -> {
                ConfigMetadata metadata = ConfigMetadata.builder().build();
                BeanUtils.copyProperties(field, metadata);
                return metadata;
            })
            .collect(Collectors.toList());
        String config = configMetadataResolver.checkAndEncrypt(metadataList, request.getConfig(), entity.getConfig());
        request.setConfig(config);
        notificationChannelService.updateFromRequest(request);
        return true;
    }

    @Override
    public boolean removeChannel(Long channelId) {
        notificationChannelService.deleteEntity(channelId);
        return true;
    }
}