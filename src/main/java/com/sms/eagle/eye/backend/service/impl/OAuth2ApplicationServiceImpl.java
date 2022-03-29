package com.sms.eagle.eye.backend.service.impl;

import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.model.OAuth2TokenResponse;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.nerko.dto.NerkoUserInfo;
import com.sms.eagle.eye.backend.nerko.service.NerkoUserService;
import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import com.sms.eagle.eye.backend.response.user.UserResponse;
import com.sms.eagle.eye.backend.service.OAuth2ApplicationService;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OAuth2ApplicationServiceImpl implements OAuth2ApplicationService {

    private final NerkoTokenService nerkoTokenService;
    private final NerkoUserService nerkoUserService;
    private final UserPermissionService userPermissionService;

    public OAuth2ApplicationServiceImpl(NerkoTokenService nerkoTokenService,
        NerkoUserService nerkoUserService,
        UserPermissionService userPermissionService) {
        this.nerkoTokenService = nerkoTokenService;
        this.nerkoUserService = nerkoUserService;
        this.userPermissionService = userPermissionService;
    }

    @Override
    public OAuth2TokenResponse getToken(String code) {
        return nerkoTokenService.getAccessTokenByCode(code);
    }

    @Override
    public UserResponse getUserInfo() {
        UserInfo userInfo = SecurityUtil.getCurrentUser();
        return UserResponse.builder()
            .username(userInfo.getUsername())
            .email(userInfo.getEmail())
            .nickname(userInfo.getNickname())
            .permissions(userPermissionService.getPermissionByEmail(userInfo.getEmail()))
            .build();
    }

    @Override
    public List<UserPermissionGroupResponse> getUsers() {
        List<NerkoUserInfo> userList = nerkoUserService.getUserList();
        Map<String, Long> permissionGroupNameMap = userPermissionService.list().stream()
            .collect(Collectors
                .toMap(UserPermissionGroupEntity::getEmail, UserPermissionGroupEntity::getPermissionGroupId));
        return userList.stream().map(nerkoUserInfo -> UserPermissionGroupResponse.builder()
            .username(nerkoUserInfo.getUsername())
            .email(nerkoUserInfo.getEmail())
            .fullName(nerkoUserInfo.getFullName())
            .workNumber(nerkoUserInfo.getWorkNumber())
            .permissionGroupId(permissionGroupNameMap.get(nerkoUserInfo.getEmail()))
            .build())
            .collect(Collectors.toList());
    }

}