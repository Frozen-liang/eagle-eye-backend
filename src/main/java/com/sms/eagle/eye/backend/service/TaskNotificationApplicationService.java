package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import java.util.List;

public interface TaskNotificationApplicationService {

    /**
     * 获取 指定任务 在 指定告警级别 下设置的 告警通知方式.
     *
     * @param taskId 任务id
     * @param alarmLevel 告警级别
     */
    List<TaskAlertNotificationResponse> getAlertNotification(Long taskId, Integer alarmLevel);

    /**
     * 添加告警方式.
     */
    boolean addAlertNotification(TaskAlertNotificationAddRequest request);

    /**
     * 根据 notificationChannelId 修改告警通知方式.
     */
    boolean updateAlertNotification(TaskAlertNotificationUpdateRequest request);

    /**
     * 根据 notificationChannelId 删除告警通知方式.
     */
    boolean deleteAlertNotification(Long alertNotificationId);

    void test(NotificationEvent event);
}
