package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import java.util.List;

public interface TaskAlertNotificationService extends IService<TaskAlertNotificationEntity> {

    TaskAlertNotificationEntity getEntityById(Long alertNotificationId);

    List<TaskAlertNotificationEntity> getByTaskIdAndAlarmLevel(Long taskId, Integer alarmLevel);

    void addByRequest(TaskAlertNotificationAddRequest request);

    void updateByRequest(TaskAlertNotificationUpdateRequest request);

    void delete(Long alertNotificationId);
}
