package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.request.task.TaskScheduleRequest;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import java.util.List;

public interface TaskApplicationService {

    CustomPage<TaskResponse> page(TaskQueryRequest request);

    String addTask(TaskBasicInfoRequest request);

    boolean updateTask(TaskBasicInfoRequest request);

    TaskPluginConfigResponse getPluginConfigByTaskId(Long taskId);

    boolean updatePluginConfig(TaskPluginConfigRequest request);

    boolean updateSchedule(TaskScheduleRequest request);

    boolean startByTaskId(Long taskId);

    boolean stopByTaskId(Long taskId);

    boolean removeTask(Long taskId);

    List<InvokeErrorRecordResponse> getErrorRecord(Long taskId);

}
