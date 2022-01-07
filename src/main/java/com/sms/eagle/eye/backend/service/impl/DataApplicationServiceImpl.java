package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.common.enums.TaskScheduleUnit;
import com.sms.eagle.eye.backend.domain.service.PluginService;
import com.sms.eagle.eye.backend.domain.service.TagService;
import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class DataApplicationServiceImpl implements DataApplicationService {

    private final PluginService pluginService;
    private final TagService tagService;

    public DataApplicationServiceImpl(PluginService pluginService,
        TagService tagService) {
        this.pluginService = pluginService;
        this.tagService = tagService;
    }

    @Override
    public List<IdNameResponse<Long>> getPluginList() {
        return pluginService.getList();
    }

    @Override
    public List<String> getProjectList() {
        return Arrays.asList("nerko", "courier");
    }

    @Override
    public List<String> getTeamList() {
        return Arrays.asList("7up", "cola", "AMP");
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

}