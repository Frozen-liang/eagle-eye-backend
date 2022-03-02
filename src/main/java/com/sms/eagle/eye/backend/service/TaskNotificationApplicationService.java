package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import java.util.List;

public interface TaskNotificationApplicationService {

    List<TaskAlertNotificationResponse> getAlertNotification(Long taskId, Integer alarmLevel);

    boolean addAlertNotification(TaskAlertNotificationAddRequest request);

    boolean updateAlertNotification(TaskAlertNotificationUpdateRequest request);

    boolean deleteAlertNotification(Long alertNotificationId);

    void test();
}
