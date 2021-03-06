package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.PERMISSION_VIEWS;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import com.sms.eagle.eye.backend.response.user.UserResponse;
import com.sms.eagle.eye.backend.service.OAuth2ApplicationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2Controller {

    private final OAuth2ApplicationService oauth2ApplicationService;

    public OAuth2Controller(OAuth2ApplicationService oauth2ApplicationService) {
        this.oauth2ApplicationService = oauth2ApplicationService;
    }

    @GetMapping("/access-token")
    public Response<String> getAccessToken(@RequestParam String code) {
        return Response.ok(oauth2ApplicationService.getToken(code).getAccessToken());
    }

    @GetMapping("/userinfo")
    public Response<UserResponse> getUserInfo() {
        return Response.ok(oauth2ApplicationService.getUserInfo());
    }

    @GetMapping("/users")
    @PreAuth(PERMISSION_VIEWS)
    public Response<List<UserPermissionGroupResponse>> getUsers() {
        return Response.ok(oauth2ApplicationService.getUsers());
    }

}