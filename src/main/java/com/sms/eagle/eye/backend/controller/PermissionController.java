package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_VIEWS;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
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
    @PreAuth(PERMISSION_VIEWS)
    public Response<List<PermissionResponse>> list() {
        return Response.ok(PermissionType.PERMISSION_RESPONSES);
    }

}