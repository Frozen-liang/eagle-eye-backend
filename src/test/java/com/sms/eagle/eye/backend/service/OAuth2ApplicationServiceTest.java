package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.service.UserPermissionGroupService;
import com.sms.eagle.eye.backend.model.OAuth2TokenResponse;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.nerko.dto.NerkoUserInfo;
import com.sms.eagle.eye.backend.nerko.service.NerkoUserService;
import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import com.sms.eagle.eye.backend.response.user.UserResponse;
import com.sms.eagle.eye.backend.service.impl.OAuth2ApplicationServiceImpl;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class OAuth2ApplicationServiceTest {

    private final NerkoTokenService nerkoTokenService = mock(NerkoTokenService.class);
    private final NerkoUserService nerkoUserService = mock(NerkoUserService.class);
    private final UserPermissionGroupService userPermissionGroupService = mock(UserPermissionGroupService.class);

    private final OAuth2ApplicationService oAuth2ApplicationService =
        spy(new OAuth2ApplicationServiceImpl(nerkoTokenService, nerkoUserService, userPermissionGroupService));

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);

    @AfterAll
    static void afterAll() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    /**
     * {@link OAuth2ApplicationServiceImpl#getToken(String)}
     *
     * 获取 access token
     */
    @Test
    void getToken_test() {
        String code = "";
        // mock nerkoTokenService.getAccessTokenByCode()
        OAuth2TokenResponse response = mock(OAuth2TokenResponse.class);
        doReturn(response).when(nerkoTokenService).getAccessTokenByCode(code);
        // 执行
        OAuth2TokenResponse result = oAuth2ApplicationService.getToken(code);
        // 验证
        assertThat(result).isEqualTo(response);
    }

    /**
     * {@link OAuth2ApplicationServiceImpl#getUserInfo()} ()}
     *
     * 获取当前请求的用户信息
     */
    @Test
    void getUserInfo_test() {
        // mock SecurityUtil.getCurrentUser()
        String username = "username";
        String email = "email";
        UserInfo userInfo = UserInfo.builder().username(username).email(email).build();
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);
        // mock userPermissionService.getPermissionByEmail()
        Set<String> permissions = mock(Set.class);
        when(userPermissionGroupService.getPermissionByEmail(email)).thenReturn(permissions);
        // 执行
        UserResponse response = oAuth2ApplicationService.getUserInfo();
        // 验证
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getPermissions()).isEqualTo(permissions);
    }

    /**
     * {@link OAuth2ApplicationServiceImpl#getUsers()}
     *
     * 获取所有的用户信息及其拥有的权限
     */
    @Test
    void getUsers_test() {
        // mock NerkoUserInfo
        String username = "username";
        String email = "email";
        NerkoUserInfo nerkoUserInfo = mock(NerkoUserInfo.class);
        when(nerkoUserInfo.getUsername()).thenReturn(username);
        when(nerkoUserInfo.getEmail()).thenReturn(email);
        // mock nerkoUserService.getUserList()
        List<NerkoUserInfo> list = new ArrayList<>(Collections.singletonList(nerkoUserInfo));
        when(nerkoUserService.getUserList()).thenReturn(list);
        // mock userPermissionService.list()
        Long permissionGroupId = 1L;
        UserPermissionGroupEntity userPermission = UserPermissionGroupEntity.builder()
            .email(email).permissionGroupId(permissionGroupId).build();
        List<UserPermissionGroupEntity> userPermissionEntities = new ArrayList<>(Collections.singletonList(userPermission));
        when(userPermissionGroupService.list()).thenReturn(userPermissionEntities);
        // 执行
        List<UserPermissionGroupResponse> users = oAuth2ApplicationService.getUsers();
        // 验证
        assertThat(users).hasSize(1);
        assertThat(users)
            .allMatch(response -> Objects.equals(permissionGroupId, response.getPermissionGroupId()))
            .allMatch(userPermissionGroupResponse -> Objects.equals(userPermissionGroupResponse.getEmail(), email));
    }
}
