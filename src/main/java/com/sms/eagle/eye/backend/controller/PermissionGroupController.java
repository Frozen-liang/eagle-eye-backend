package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_GROUP_ADD;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_GROUP_DELETE;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_GROUP_EDIT;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_VIEWS;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.permission.AddOrRemovePermissionRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import com.sms.eagle.eye.backend.service.PermissionGroupApplicationService;
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
@RequestMapping("/v1/permission-group")
public class PermissionGroupController {

    private final PermissionGroupApplicationService permissionGroupApplicationService;

    public PermissionGroupController(PermissionGroupApplicationService permissionGroupApplicationService) {
        this.permissionGroupApplicationService = permissionGroupApplicationService;
    }

    @GetMapping("/page")
    @PreAuth(PERMISSION_VIEWS)
    public Response<CustomPage<PermissionGroupResponse>> page(PermissionGroupQueryRequest pageRequest) {
        return Response.ok(permissionGroupApplicationService.page(pageRequest));
    }

    @GetMapping("/list")
    public Response<List<PermissionGroupResponse>> list() {
        return Response.ok(permissionGroupApplicationService.list());
    }

    @PostMapping
    @PreAuth(PERMISSION_GROUP_ADD)
    public Response<String> add(@Validated(InsertGroup.class) @RequestBody PermissionGroupRequest request) {
        return Response.ok(permissionGroupApplicationService.save(request));
    }

    @PutMapping
    @PreAuth(PERMISSION_GROUP_EDIT)
    public Response<Boolean> update(@Validated(UpdateGroup.class) @RequestBody PermissionGroupRequest request) {
        return Response.ok(permissionGroupApplicationService.update(request));
    }

    @DeleteMapping("/{id}")
    @PreAuth(PERMISSION_GROUP_DELETE)
    public Response<Boolean> delete(@PathVariable Long id) {
        return Response.ok(permissionGroupApplicationService.delete(id));
    }

    @PostMapping("/addPermission")
    @PreAuth(PERMISSION_GROUP_EDIT)
    public Response<Boolean> addPermission(@Validated @RequestBody AddOrRemovePermissionRequest request) {
        return Response.ok(permissionGroupApplicationService.addPermission(request));
    }

    @PostMapping("/removePermission")
    @PreAuth(PERMISSION_GROUP_EDIT)
    public Response<Boolean> removePermission(@Validated @RequestBody AddOrRemovePermissionRequest request) {
        return Response.ok(permissionGroupApplicationService.removePermission(request));
    }

}