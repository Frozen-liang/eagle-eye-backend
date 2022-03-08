package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import com.sms.eagle.eye.backend.service.PermissionApplicationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/permission")
public class PermissionController {

    private final PermissionApplicationService permissionApplicationService;

    public PermissionController(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    @GetMapping("/list")
    public Response<List<PermissionResponse>> list(String name) {
        return Response.ok(permissionApplicationService.list(name));
    }


}