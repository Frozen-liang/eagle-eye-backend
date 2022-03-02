package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.AlarmLevel;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
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

    public DataApplicationServiceImpl(PluginService pluginService,
        TagService tagService,
        TaskService taskService,
        ThirdPartyMappingService thirdPartyMappingService) {
        this.pluginService = pluginService;
        this.tagService = tagService;
        this.taskService = taskService;
        this.thirdPartyMappingService = thirdPartyMappingService;
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
     * 如果 thirdPartyMapping 不存在 尝试直接当作 任务id 返回.
     */
    @Override
    public Optional<Long> getTaskByMappingId(String uniqueValue) {
        Optional<Long> optional = thirdPartyMappingService.getTaskIdByPluginSystemUnionId(uniqueValue);
        if (optional.isPresent()) {
            return optional;
        } else {
            return Optional.ofNullable(Try.of(() -> Long.parseLong(uniqueValue)).getOrNull());
        }
    }

    @Override
    public Optional<Long> getTaskIdByTaskName(String uniqueValue) {
        return taskService.getIdByName(uniqueValue);
    }

}