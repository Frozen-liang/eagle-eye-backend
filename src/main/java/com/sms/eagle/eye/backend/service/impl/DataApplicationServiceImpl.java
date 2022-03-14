package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.service.PluginAlarmLevelMappingService;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.domain.service.TaskAlertRuleService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.model.TaskAlarmInfo;
import com.sms.eagle.eye.backend.response.task.AlarmLevelResponse;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DataApplicationServiceImpl implements DataApplicationService {

    private final PluginService pluginService;
    private final TagService tagService;
    private final TaskService taskService;
    private final ThirdPartyMappingService thirdPartyMappingService;
    private final TaskAlertRuleService taskAlertRuleService;
    private final PluginAlarmLevelMappingService pluginAlarmLevelMappingService;

    public DataApplicationServiceImpl(PluginService pluginService,
        TagService tagService,
        TaskService taskService,
        ThirdPartyMappingService thirdPartyMappingService,
        TaskAlertRuleService taskAlertRuleService,
        PluginAlarmLevelMappingService pluginAlarmLevelMappingService) {
        this.pluginService = pluginService;
        this.tagService = tagService;
        this.taskService = taskService;
        this.thirdPartyMappingService = thirdPartyMappingService;
        this.taskAlertRuleService = taskAlertRuleService;
        this.pluginAlarmLevelMappingService = pluginAlarmLevelMappingService;
    }

    @Override
    public List<IdNameResponse<Long>> getPluginList() {
        return pluginService.getList();
    }

    @Override
    public List<IdNameResponse<Integer>> getScheduleUnitList() {
        return Arrays.stream(TaskScheduleUnit.values())
            .map(taskScheduleUnit -> IdNameResponse.<Integer>builder()
                .id(taskScheduleUnit.getId())
                .name(taskScheduleUnit.getName())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<IdNameResponse<Long>> getTagList() {
        return tagService.getList();
    }

    @Override
    public List<IdNameResponse<Integer>> getTaskStatusList() {
        return Arrays.stream(TaskStatus.values())
            .map(taskStatus -> IdNameResponse.<Integer>builder()
                .id(taskStatus.getValue())
                .name(taskStatus.getName())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<AlarmLevelResponse> getAlarmLevelResponse() {
        return Arrays.stream(AlarmLevel.values())
            .map(alarmLevel -> AlarmLevelResponse.builder()
                .name(alarmLevel.getName())
                .value(alarmLevel.getValue())
                .isAlarm(alarmLevel.getIsAlarm())
                .color(alarmLevel.getColor())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<IdNameResponse<Integer>> getTemplateType() {
        return Arrays.stream(NotificationTemplateType.values())
            .map(notificationTemplateType -> IdNameResponse.<Integer>builder()
                .id(notificationTemplateType.getValue())
                .name(notificationTemplateType.getName())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * TODO 两种情形分开.
     *
     * <p>根据 mappingId 获取 {@link TaskAlarmInfo#getTaskId()},
     * 再根据 mappingLevel 获取 {@link TaskAlarmInfo#getAlarmLevel()}
     *
     * <p>或者根据 taskAlertRuleId 获取 {@link TaskAlarmInfo}
     *
     * @param uniqueValue 与任务相关的mappingId，或是taskAlertRuleId
     * @param mappingLevel 第三方告警级别，uniqueValue 为 mappingId 时该字段必须提供
     */
    @Override
    public Optional<TaskAlarmInfo> getTaskByMappingId(String uniqueValue, String mappingLevel) {
        Optional<Long> optional = thirdPartyMappingService.getTaskIdByPluginSystemUnionId(uniqueValue);
        if (optional.isPresent()) {
            TaskEntity task = taskService.getEntityById(optional.get());
            return pluginAlarmLevelMappingService
                .getSystemLevelByPluginIdAndMappingLevel(task.getPluginId(), mappingLevel)
                .map(systemLevel -> TaskAlarmInfo.builder().taskId(task.getId()).alarmLevel(systemLevel).build());
        } else {
            Optional<TaskAlertRuleEntity> taskAlertRuleOption = taskAlertRuleService
                .getByTaskAlertRuleId(Try.of(() -> Long.parseLong(uniqueValue)).getOrNull());
            return taskAlertRuleOption.map(alertRule -> TaskAlarmInfo.builder()
                .taskId(alertRule.getTaskId()).alarmLevel(alertRule.getAlarmLevel()).build());
        }
    }

    /**
     * 根据任务名称获取 {@link TaskAlarmInfo#getTaskId()}
     * 再根据第三方告警级别获取 {@link TaskAlarmInfo#getAlarmLevel()}.
     *
     * @param uniqueValue 任务名称
     * @param mappingLevel 第三方告警级别
     */
    @Override
    public Optional<TaskAlarmInfo> getTaskIdByTaskName(String uniqueValue, String mappingLevel) {
        Optional<TaskEntity> taskOptional = taskService.getEntityByName(uniqueValue);
        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }
        TaskEntity task = taskOptional.get();
        Optional<Integer> systemLevelOptional = pluginAlarmLevelMappingService
            .getSystemLevelByPluginIdAndMappingLevel(task.getPluginId(), mappingLevel);
        if (systemLevelOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(TaskAlarmInfo.builder()
            .taskId(task.getId())
            .alarmLevel(systemLevelOptional.get())
            .build());
    }

}