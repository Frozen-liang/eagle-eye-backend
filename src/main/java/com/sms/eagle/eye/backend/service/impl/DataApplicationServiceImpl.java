package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.domain.service.ThirdPartyMappingService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.service.DataApplicationService;
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
    public Optional<Long> getTaskByMappingId(String uniqueValue) {
        return thirdPartyMappingService.getTaskIdByPluginSystemUnionId(uniqueValue);
    }

    @Override
    public Optional<Long> getTaskIdByTaskName(String uniqueValue) {
        return taskService.getIdByName(uniqueValue);
    }

}