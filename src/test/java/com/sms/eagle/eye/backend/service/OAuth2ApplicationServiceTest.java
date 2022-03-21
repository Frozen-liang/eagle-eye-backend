package com.sms.eagle.eye.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.config.NerkoOAuth2Properties;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionEntity;
import com.sms.eagle.eye.backend.domain.service.UserPermissionService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NerkoUserResponse;
import com.sms.eagle.eye.backend.model.OAuth2AccessTokenResponse;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.response.user.UserPermissionGroupResponse;
import com.sms.eagle.eye.backend.response.user.UserResponse;
import com.sms.eagle.eye.backend.service.OAuth2ApplicationService;
import com.sms.eagle.eye.backend.service.impl.OAuth2ApplicationServiceImpl;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class OAuth2ApplicationServiceTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final NerkoOAuth2Properties nerkoOAuth2Properties = createNerkoOAuth2Properties();
    private final UserPermissionService userPermissionService = mock(UserPermissionService.class);
    private final OAuth2ApplicationService oAuth2ApplicationService =
        new OAuth2ApplicationServiceImpl(restTemplate, nerkoOAuth2Properties, userPermissionService);
    private static final String CODE = "code";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);

    static {
        UserInfo userInfo = UserInfo.builder().username("username").nickname("nickname").email("email@test.com")
            .build();
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrentUser).thenReturn(userInfo);
    }

    @AfterAll
    static void afterAll() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    void getAccessToken_test() {
        // mock
        mock_restTemplate();
        // 执行
        String accessToken = oAuth2ApplicationService.getAccessToken(CODE);
        // 验证
        assertThat(accessToken).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    void getAccessToken_exception_test() {
        // mock
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any()))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        // 验证异常
        assertThatThrownBy(() -> oAuth2ApplicationService.getAccessToken(CODE)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    void getUserInfo_test() {
        // mock
        when(userPermissionService.getPermissionByEmail(anyString())).thenReturn(Collections.emptyList());
        // 执行
        UserResponse userInfo = oAuth2ApplicationService.getUserInfo();
        // 验证
        assertThat(userInfo).isNotNull();
    }

    @Test
    void getUsers_test() {
        // mock
        mock_restTemplate();
        String email = "email@test.com";
        when(userPermissionService.list()).thenReturn(List.of(
            UserPermissionEntity.builder().email(email).permissionGroupId(1L).build()));
        NerkoUserResponse nerkoUserResponse = new NerkoUserResponse();
        nerkoUserResponse
            .setData(Collections.singletonList(UserPermissionGroupResponse.builder().email(email).build()));
        ResponseEntity<NerkoUserResponse> responseEntity = new ResponseEntity<>(nerkoUserResponse,
            HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
            .thenReturn(responseEntity);
        // 执行
        List<UserPermissionGroupResponse> users = oAuth2ApplicationService.getUsers();
        // 验证
        assertThat(users).hasSize(1);
        assertThat(users).allMatch(userPermissionGroupResponse -> email.equals(userPermissionGroupResponse.getEmail()));
    }

    @Test
    void getUsers_exception_test() {
        // mock
        mock_restTemplate();
        String email = "email@test.com";
        when(userPermissionService.list()).thenReturn(List.of(
            UserPermissionEntity.builder().email(email).permissionGroupId(1L).build()));
        NerkoUserResponse nerkoUserResponse = new NerkoUserResponse();
        nerkoUserResponse
            .setData(Collections.singletonList(UserPermissionGroupResponse.builder().email(email).build()));
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
            .thenThrow(new RestClientException(""));
        // 验证异常
        assertThatThrownBy(oAuth2ApplicationService::getUsers).isInstanceOf(EagleEyeException.class);
    }

    private void mock_restTemplate() {
        OAuth2AccessTokenResponse auth2AccessTokenResponse = OAuth2AccessTokenResponse.builder()
            .accessToken(ACCESS_TOKEN)
            .refreshToken("refreshToken").build();
        when(restTemplate.postForObject(anyString(), any(HttpEntity.class), any()))
            .thenReturn(auth2AccessTokenResponse);
    }

    private NerkoOAuth2Properties createNerkoOAuth2Properties() {
        NerkoOAuth2Properties nerkoOAuth2Properties = new NerkoOAuth2Properties();
        nerkoOAuth2Properties.setClientId("clientId");
        nerkoOAuth2Properties.setClientSecret("clientSecret");
        nerkoOAuth2Properties.setTokenEndpoint("http://localhost/oauth/auth");
        nerkoOAuth2Properties.setUsersEndpoint("http://localhost/v1/user");
        return nerkoOAuth2Properties;
    }

}
