package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.service.TaskAlertNotificationService;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.impl.ChannelProxy;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import com.sms.eagle.eye.backend.service.TaskNotificationApplicationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TaskNotificationApplicationServiceImpl implements TaskNotificationApplicationService {

    private final Channel channel;
    private final ConfigMetadataResolver configMetadataResolver;
    private final ConfigMetadataConverter configMetadataConverter;
    private final TaskAlertNotificationService taskAlertNotificationService;

    public TaskNotificationApplicationServiceImpl(
        @Qualifier(ChannelProxy.CHANNEL_PROXY) Channel channel, ConfigMetadataResolver configMetadataResolver,
        ConfigMetadataConverter configMetadataConverter,
        TaskAlertNotificationService taskAlertNotificationService) {
        this.channel = channel;
        this.configMetadataResolver = configMetadataResolver;
        this.configMetadataConverter = configMetadataConverter;
        this.taskAlertNotificationService = taskAlertNotificationService;
    }

    @Override
    public List<TaskAlertNotificationResponse> getAlertNotification(Long taskId, Integer alarmLevel) {
        List<TaskAlertNotificationEntity> taskAlertNotifications = taskAlertNotificationService
            .getByTaskIdAndAlarmLevel(taskId, alarmLevel);
        if (CollectionUtils.isNotEmpty(taskAlertNotifications)) {
            return taskAlertNotifications.stream().map(taskAlertNotificationEntity -> {
                Map<String, Object> map = configMetadataResolver
                    .convertConfigToMap(taskAlertNotificationEntity.getChannelInput());

                TaskAlertNotificationResponse response = TaskAlertNotificationResponse.builder().build();
                response.setAlertNotificationId(taskAlertNotificationEntity.getId());
                response.setChannelType(taskAlertNotificationEntity.getChannelType());
                response.setContent(taskAlertNotificationEntity.getContent());

                List<ChannelFieldResponse> channelFields = channel.getTaskInputFields(
                    taskAlertNotificationEntity.getChannelType());
                List<ChannelFieldWithValueResponse> inputFields = channelFields.stream().map(channelFieldResponse -> {
                    ChannelFieldWithValueResponse valueResponse = ChannelFieldWithValueResponse.builder().build();
                    BeanUtils.copyProperties(channelFieldResponse, valueResponse);
                    Object value = configMetadataResolver.decryptToFrontendValue(
                        configMetadataConverter.fromChannelField(channelFieldResponse), map);
                    valueResponse.setValue(value);
                    return valueResponse;
                }).collect(Collectors.toList());
                response.setInput(inputFields);
                return response;
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean addAlertNotification(TaskAlertNotificationAddRequest request) {
        List<ConfigMetadata> metadataList = channel.getTaskInputFields(request.getChannelType()).stream()
                .map(configMetadataConverter::fromChannelField).collect(Collectors.toList());
        String encryptValue = configMetadataResolver.checkAndEncrypt(metadataList, request.getChannelInput());
        request.setChannelInput(encryptValue);
        taskAlertNotificationService.addByRequest(request);
        return true;
    }

    @Override
    public boolean updateAlertNotification(TaskAlertNotificationUpdateRequest request) {
        TaskAlertNotificationEntity entity = taskAlertNotificationService
            .getEntityById(request.getAlertNotificationId());
        List<ConfigMetadata> metadataList = channel.getTaskInputFields(entity.getChannelType()).stream()
            .map(configMetadataConverter::fromChannelField).collect(Collectors.toList());
        String encryptValue = configMetadataResolver.checkAndEncrypt(metadataList, request.getChannelInput());
        request.setChannelInput(encryptValue);
        taskAlertNotificationService.updateByRequest(request);
        return true;
    }

    @Override
    public boolean deleteAlertNotification(Long alertNotificationId) {
        taskAlertNotificationService.delete(alertNotificationId);
        return true;
    }

    @Override
    public void test(NotificationEvent event) {
        channel.notify(event);
    }
}