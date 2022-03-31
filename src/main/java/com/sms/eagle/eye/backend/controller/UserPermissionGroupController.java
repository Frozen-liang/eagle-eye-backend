package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.USER_EDIT;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.request.permission.UserPermissionGroupRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.service.UserPermissionGroupApplicationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-permission-group")
public class UserPermissionGroupController {

    private final UserPermissionGroupApplicationService userPermissionGroupApplicationService;

    public UserPermissionGroupController(
        UserPermissionGroupApplicationService userPermissionGroupApplicationService) {
        this.userPermissionGroupApplicationService = userPermissionGroupApplicationService;
    }

    @PutMapping
    @PreAuth(USER_EDIT)
    public Response<Boolean> addOrUpdate(@Validated @RequestBody UserPermissionGroupRequest request) {
        return Response.ok(userPermissionGroupApplicationService.addOrUpdate(request));
    }
}
