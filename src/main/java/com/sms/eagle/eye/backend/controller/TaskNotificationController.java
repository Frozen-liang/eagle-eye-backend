package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import com.sms.eagle.eye.backend.response.task.TaskAlertNotificationResponse;
import com.sms.eagle.eye.backend.service.TaskNotificationApplicationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控告警方式管理.
 */
@Slf4j
@RestController
@RequestMapping("/v1/task")
public class TaskNotificationController {

    private final TaskNotificationApplicationService taskNotificationApplicationService;

    public TaskNotificationController(
        TaskNotificationApplicationService taskNotificationApplicationService) {
        this.taskNotificationApplicationService = taskNotificationApplicationService;
    }

    @GetMapping("/alert-notification/{taskId}")
    public Response<List<TaskAlertNotificationResponse>> getAlertNotification(
        @PathVariable Long taskId, @RequestParam Integer alarmLevel) {
        return Response.ok(taskNotificationApplicationService.getAlertNotification(taskId, alarmLevel));
    }

    @PostMapping("/alert-notification")
    public Response<Boolean> addAlertNotification(@RequestBody TaskAlertNotificationAddRequest request) {
        return Response.ok(taskNotificationApplicationService.addAlertNotification(request));
    }

    @PutMapping("/alert-notification")
    public Response<Boolean> updateAlertNotification(@RequestBody TaskAlertNotificationUpdateRequest request) {
        return Response.ok(taskNotificationApplicationService.updateAlertNotification(request));
    }

    @DeleteMapping("/{alertNotificationId}")
    public Response<Boolean> delete(@PathVariable Long alertNotificationId) {
        return Response.ok(taskNotificationApplicationService.deleteAlertNotification(alertNotificationId));
    }

    @GetMapping("/test")
    public Response<Boolean> test() {
        taskNotificationApplicationService.test();
        return Response.ok(Boolean.TRUE);
    }

}