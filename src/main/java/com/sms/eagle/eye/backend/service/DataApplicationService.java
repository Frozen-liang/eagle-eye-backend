package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.IdNameResponse;
import com.sms.eagle.eye.backend.response.task.AlarmLevelResponse;
import java.util.List;
import java.util.Optional;

public interface DataApplicationService {

    List<IdNameResponse<Long>> getPluginList();

    List<IdNameResponse<Integer>> getScheduleUnitList();

    List<IdNameResponse<Long>> getTagList();

    List<IdNameResponse<Integer>> getTaskStatusList();

    List<AlarmLevelResponse> getAlarmLevelResponse();

    List<IdNameResponse<Integer>> getTemplateType();

    Optional<Long> getTaskByMappingId(String uniqueValue);

    Optional<Long> getTaskIdByTaskName(String uniqueValue);
}
