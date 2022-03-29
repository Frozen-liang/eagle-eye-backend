package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.TASK_GROUP_ADD;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.TASK_GROUP_DELETE;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.TASK_GROUP_EDIT;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.TASK_GROUP_VIEWS;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.response.task.TaskGroupTreeResponse;
import com.sms.eagle.eye.backend.service.TaskGroupApplicationService;
import java.util.List;
import org.springframework.validation.annotation.Validated;
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
    @PreAuth(TASK_GROUP_VIEWS)
    public Response<List<TaskGroupResponse>> list(Long parentId) {
        return Response.ok(taskGroupApplicationService.getGroupListByParentId(parentId));
    }

    @GetMapping("/tree-list")
    @PreAuth(TASK_GROUP_VIEWS)
    public Response<List<TaskGroupTreeResponse>> getTreeList() {
        return Response.ok(taskGroupApplicationService.getTaskGroupTreeList());
    }

    @PostMapping
    @PreAuth(TASK_GROUP_ADD)
    public Response<Boolean> add(@Validated(value = InsertGroup.class) @RequestBody TaskGroupRequest request) {
        return Response.ok(taskGroupApplicationService.addGroup(request));
    }

    @PutMapping
    @PreAuth(TASK_GROUP_EDIT)
    public Response<Boolean> update(@Validated(value = UpdateGroup.class) @RequestBody TaskGroupRequest request) {
        return Response.ok(taskGroupApplicationService.updateGroup(request));
    }

    @PutMapping("/name")
    @PreAuth(TASK_GROUP_EDIT)
    public Response<Boolean> rename(@Validated(value = UpdateGroup.class) @RequestBody TaskGroupRequest request) {
        return Response.ok(taskGroupApplicationService.rename(request));
    }

    @DeleteMapping("/{id}")
    @PreAuth(TASK_GROUP_DELETE)
    public Response<Boolean> delete(@PathVariable Long id) {
        return Response.ok(taskGroupApplicationService.removeGroup(id));
    }

    @DeleteMapping("/batch/{ids}")
    @PreAuth(TASK_GROUP_DELETE)
    public Response<Boolean> deleteByIds(@PathVariable List<Long> ids) {
        return Response.ok(taskGroupApplicationService.removeGroupByIds(ids));
    }

}