package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskPluginConfigRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.request.task.TaskScheduleRequest;
import com.sms.eagle.eye.backend.response.task.TaskPluginConfigResponse;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.service.TaskApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控任务管理.
 */
@Slf4j
@RestController
@RequestMapping("/v1/task")
public class TaskController {

    private final TaskApplicationService taskApplicationService;

    public TaskController(TaskApplicationService taskApplicationService) {
        this.taskApplicationService = taskApplicationService;
    }

    /**
     * 分页查询监控任务列表.
     */
    @GetMapping("/page")
    public Response<CustomPage<TaskResponse>> page(TaskQueryRequest request) {
        return Response.ok(taskApplicationService.page(request));
    }

    /**
     * 添加监控任务基本信息.
     */
    @PostMapping
    public Response<String> add(@Validated(value = InsertGroup.class) @RequestBody TaskBasicInfoRequest request) {
        return Response.ok(taskApplicationService.addTask(request));
    }

    /**
     * 修改监控任务基本信息.
     */
    @PutMapping
    public Response<Boolean> update(@Validated(value = UpdateGroup.class) @RequestBody TaskBasicInfoRequest request) {
        return Response.ok(taskApplicationService.updateTask(request));
    }

    /**
     * 根据id查询任务使用插件的配置.
     */
    @GetMapping("/plugin-config/{id}")
    public Response<TaskPluginConfigResponse> getPluginConfigById(@PathVariable Long id) {
        return Response.ok(taskApplicationService.getPluginConfigByTaskId(id));
    }

    /**
     * 修改任务使用插件的配置.
     */
    @PutMapping("/plugin-config")
    public Response<Boolean> updatePluginConfig(@Validated @RequestBody TaskPluginConfigRequest request) {
        return Response.ok(taskApplicationService.updatePluginConfig(request));
    }

    /**
     * 修改监控任务的定时计划.
     */
    @PutMapping("/schedule")
    public Response<Boolean> updateSchedule(@RequestBody TaskScheduleRequest request) {
        return Response.ok(taskApplicationService.updateSchedule(request));
    }

    /**
     * 根据任务id启动.
     */
    @PutMapping("/start/{id}")
    public Response<Boolean> startTask(@PathVariable Long id) {
        return Response.ok(taskApplicationService.startByTaskId(id));
    }

    /**
     * 根据任务id停止.
     */
    @PutMapping("/stop/{id}")
    public Response<Boolean> stopTask(@PathVariable Long id) {
        return Response.ok(taskApplicationService.stopByTaskId(id));
    }

    /**
     * 根据id删除任务.
     */
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        return Response.ok(taskApplicationService.removeTask(id));
    }

    @PostMapping("/invoke-result")
    public Response<Boolean> resolveInvokeResult(@Validated @RequestBody LambdaInvokeResult request) {
        return Response.ok(taskApplicationService.resolveInvokeResult(request));
    }

}