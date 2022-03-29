package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.enums.PermissionType;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/permission")
public class PermissionController {

    @GetMapping("/list")
    public Response<List<PermissionResponse>> list() {
        return Response.ok(PermissionType.PERMISSION_RESPONSES);
    }

}