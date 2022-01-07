package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.IdNameResponse;
import java.util.List;

public interface DataApplicationService {

    List<IdNameResponse<Long>> getPluginList();

    List<String> getProjectList();

    List<String> getTeamList();

    List<IdNameResponse<Integer>> getScheduleUnitList();

    List<IdNameResponse<Long>> getTagList();
}
