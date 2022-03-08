package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.request.task.TaskAlertRuleRequest;
import java.util.List;
import java.util.Optional;

public interface TaskAlertRuleService extends IService<TaskAlertRuleEntity> {

    Optional<TaskAlertRuleEntity> getByTaskIdAndAlertLevel(Long taskId, Integer alertLevel);

    void updateByRequest(TaskAlertRuleRequest request);

    List<TaskAlertRuleEntity> getByTaskId(Long taskId);
}
