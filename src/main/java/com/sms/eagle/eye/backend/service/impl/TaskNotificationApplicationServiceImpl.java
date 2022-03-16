package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.convert.ChannelFieldConverter;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.convert.TaskAlertNotificationChannelConverter;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TaskNotificationApplicationServiceImpl implements TaskNotificationApplicationService {

    private final Channel channel;
    private final ConfigMetadataResolver configMetadataResolver;
    private final ConfigMetadataConverter configMetadataConverter;
    private final ChannelFieldConverter channelFieldConverter;
    private final TaskAlertNotificationChannelConverter taskAlertNotificationChannelConverter;
    private final TaskAlertNotificationService taskAlertNotificationService;

    public TaskNotificationApplicationServiceImpl(
        @Qualifier(ChannelProxy.CHANNEL_PROXY) Channel channel, ConfigMetadataResolver configMetadataResolver,
        ConfigMetadataConverter configMetadataConverter,
        ChannelFieldConverter channelFieldConverter,
        TaskAlertNotificationChannelConverter taskAlertNotificationChannelConverter,
        TaskAlertNotificationService taskAlertNotificationService) {
        this.channel = channel;
        this.configMetadataResolver = configMetadataResolver;
        this.configMetadataConverter = configMetadataConverter;
        this.channelFieldConverter = channelFieldConverter;
        this.taskAlertNotificationChannelConverter = taskAlertNotificationChannelConverter;
        this.taskAlertNotificationService = taskAlertNotificationService;
    }

    /**
     * 获取 指定任务 在 指定告警级别 下设置的 告警通知方式.
     *
     * <p>从数据库中取到的 {@link TaskAlertNotificationEntity#getChannelInput()}，
     * 需要将其转换成 {@link ChannelFieldWithValueResponse} 列表。
     *
     * @param taskId     任务id
     * @param alarmLevel 告警级别
     */
    @Override
    public List<TaskAlertNotificationResponse> getAlertNotification(Long taskId, Integer alarmLevel) {
        List<TaskAlertNotificationEntity> taskAlertNotifications = taskAlertNotificationService
            .getByTaskIdAndAlarmLevel(taskId, alarmLevel);
        return taskAlertNotifications.stream().map(entity -> {
            TaskAlertNotificationResponse response = taskAlertNotificationChannelConverter.toResponse(entity);
            response.setInput(convertToResponseWithValue(entity.getChannelInput(), entity.getChannelType()));
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 根据 通道类型 以及 用户填写的所有字段的值，转换成 前端表单展示所需的元数据.
     *
     *  <p>根据 channelType 获取 输入表单的元数据信息({@link ChannelFieldResponse}),
     *  再通过{@link ConfigMetadataResolver#decryptToFrontendValue}从 input 中得到表单的值.
     *
     * @param input 所有字段的值
     * @param channelType 通道类型
     */
    private List<ChannelFieldWithValueResponse> convertToResponseWithValue(String input, Integer channelType) {
        Map<String, Object> map = configMetadataResolver.convertConfigToMap(input);
        List<ChannelFieldResponse> channelFields = channel.getTaskInputFields(channelType);
        return channelFields.stream().map(channelFieldResponse -> {
            ConfigMetadata metadata = configMetadataConverter.fromChannelField(channelFieldResponse);
            Object value = configMetadataResolver.decryptToFrontendValue(metadata, map);
            return channelFieldConverter.fillValueToResponse(channelFieldResponse, value);
        }).collect(Collectors.toList());
    }

    /**
     * 添加告警方式
     *
     * <p>为 某个任务下 某个级别的告警 添加一种 通知方式.
     */
    @Override
    public boolean addAlertNotification(TaskAlertNotificationAddRequest request) {
        List<ConfigMetadata> metadataList = channel.getTaskInputFields(request.getChannelType()).stream()
            .map(configMetadataConverter::fromChannelField).collect(Collectors.toList());
        String encryptValue = configMetadataResolver.checkAndEncrypt(metadataList, request.getChannelInput());
        request.setChannelInput(encryptValue);
        taskAlertNotificationService.addByRequest(request);
        return true;
    }

    /**
     * 根据 notificationChannelId 修改告警通知方式.
     */
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

    /**
     * 根据 notificationChannelId 删除告警通知方式.
     */
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