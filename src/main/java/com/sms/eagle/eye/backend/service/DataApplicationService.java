package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.IdNameResponse;
import java.util.List;
import java.util.Optional;

public interface DataApplicationService {

    List<IdNameResponse<Long>> getPluginList();

    List<IdNameResponse<Integer>> getScheduleUnitList();

    List<IdNameResponse<Long>> getTagList();

    Optional<Long> getTaskByMappingId(String uniqueValue);

    Optional<Long> getTaskIdByTaskName(String uniqueValue);
}
