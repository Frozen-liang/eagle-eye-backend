package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.request.permission.UserPermissionRequest;
import com.sms.eagle.eye.backend.service.UserPermissionApplicationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user-permission")
public class UserPermissionController {

    private final UserPermissionApplicationService userPermissionApplicationService;

    public UserPermissionController(
        UserPermissionApplicationService userPermissionApplicationService) {
        this.userPermissionApplicationService = userPermissionApplicationService;
    }

    @PutMapping
    public Response<Boolean> addOrUpdate(@Validated UserPermissionRequest request) {
        return Response.ok(userPermissionApplicationService.addOrUpdate(request));
    }
}
