package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.service.TaskGroupApplicationService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/group")
public class TaskGroupController {

    private final TaskGroupApplicationService taskGroupApplicationService;

    public TaskGroupController(TaskGroupApplicationService taskGroupApplicationService) {
        this.taskGroupApplicationService = taskGroupApplicationService;
    }

    @GetMapping("/list")
    public Response<List<TaskGroupResponse>> list() {
        return Response.ok(taskGroupApplicationService.getTreeList());
    }

    @PostMapping
    public Response<Boolean> add(@RequestBody TaskGroupRequest request) {
        return Response.ok(taskGroupApplicationService.addGroup(request));
    }

    @PutMapping
    public Response<Boolean> update(@RequestBody TaskGroupRequest request) {
        return Response.ok(taskGroupApplicationService.updateGroup(request));
    }

    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        return Response.ok(taskGroupApplicationService.removeGroup(id));
    }

}