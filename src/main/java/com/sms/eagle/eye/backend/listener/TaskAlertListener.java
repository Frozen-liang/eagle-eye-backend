package com.sms.eagle.eye.backend.listener;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.convert.ConfigMetadataConverter;
import com.sms.eagle.eye.backend.domain.entity.NotificationChannelEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.NotificationChannelService;
import com.sms.eagle.eye.backend.domain.service.TaskAlertNotificationService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.event.TaskAlertEvent;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.notification.Channel;
import com.sms.eagle.eye.backend.notification.dto.AlertInfo;
import com.sms.eagle.eye.backend.notification.impl.ChannelProxy;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskAlertListener {

    private final Channel channel;
    private final TaskService taskService;
    private final ConfigMetadataConverter converter;
    private final ConfigMetadataResolver configMetadataResolver;
    private final NotificationChannelService notificationChannelService;
    private final TaskAlertNotificationService taskAlertNotificationService;

    public TaskAlertListener(@Qualifier(ChannelProxy.CHANNEL_PROXY) Channel channel, TaskService taskService,
        ConfigMetadataConverter converter,
        ConfigMetadataResolver configMetadataResolver,
        NotificationChannelService notificationChannelService,
        TaskAlertNotificationService taskAlertNotificationService) {
        this.channel = channel;
        this.taskService = taskService;
        this.converter = converter;
        this.configMetadataResolver = configMetadataResolver;
        this.notificationChannelService = notificationChannelService;
        this.taskAlertNotificationService = taskAlertNotificationService;
    }

    @EventListener
    public void invokeTaskAlert(TaskAlertEvent event) {
        // 更新任务告警级别.
        taskService.updateTaskEntity(TaskEntity.builder().id(event.getTask().getId())
            .alarmLevel(event.getAlarmLevel()).build());
        // 发送告警消息.
        AlertInfo alertInfo = AlertInfo.builder()
            .taskName(event.getTask().getName())
            .project(event.getTask().getProject())
            .alarmLevel(AlarmLevel.resolve(event.getAlarmLevel()).getName())
            .alarmMessage(event.getAlarmMessage())
            .build();
        List<TaskAlertNotificationEntity> alertNotifications = taskAlertNotificationService
            .getByTaskIdAndAlarmLevel(event.getTask().getId(), event.getAlarmLevel());
        for (TaskAlertNotificationEntity notification : alertNotifications) {
            try {
                String channelConfig = Optional.ofNullable(notification.getNotificationChannelId())
                    .map(notificationChannelService::getEntityById)
                    .map(NotificationChannelEntity::getConfig)
                    .map(cof -> configMetadataResolver.decryptToString(
                        channel.getConfigFields(notification.getChannelType()).stream()
                            .map(converter::fromChannelField).collect(Collectors.toList()), cof)).orElse(null);

                String channelInput = configMetadataResolver.decryptToString(
                    channel.getTaskInputFields(notification.getChannelType())
                        .stream().map(converter::fromChannelField)
                        .collect(Collectors.toList()), notification.getChannelInput());

                channel.notify(NotificationEvent.builder()
                    .channelType(notification.getChannelType())
                    .contentTemplate(notification.getContent())
                    .channelConfig(channelConfig)
                    .channelInput(channelInput)
                    // TODO 存TaskAlertNotificationEntity里.
                    .variableKey(NotificationTemplateType.ALERT.getVariableKey())
                    .variable(alertInfo)
                    .build());
            } catch (Exception exception) {
                log.error("Failed to alert notify", exception);
            }
        }

    }
}