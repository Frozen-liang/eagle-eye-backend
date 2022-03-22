package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.config.NerkoOAuth2Configuration.ACCESS_TOKEN;
import static com.sms.eagle.eye.backend.exception.ErrorCode.OAUTH_CODE_ERROR;

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
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OAuth2ApplicationServiceImpl implements OAuth2ApplicationService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String CODE = "code";
    private static final String SCOPE = "scope";
    private static final String SCOPE_VALUE = "user-management:GET";
    private String usersAccessToken;

    private final RestTemplate restTemplate;
    private final NerkoOAuth2Properties nerkoOAuth2Properties;
    private final UserPermissionService userPermissionService;


    public OAuth2ApplicationServiceImpl(@Qualifier(ACCESS_TOKEN) RestTemplate restTemplate,
        NerkoOAuth2Properties nerkoOAuth2Properties,
        UserPermissionService userPermissionService) {
        this.restTemplate = restTemplate;
        this.nerkoOAuth2Properties = nerkoOAuth2Properties;
        this.userPermissionService = userPermissionService;
    }

    @Override
    public String getAccessToken(String code) {
        return getAccessTokenByGrantType(AUTHORIZATION_CODE, code);
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
        usersAccessToken = StringUtils
            .defaultIfBlank(usersAccessToken, getAccessTokenByGrantType(CLIENT_CREDENTIALS, null));
        try {
            List<UserPermissionGroupResponse> users = queryUser(usersAccessToken);
            Map<String, Long> permissionGroupNameMap = userPermissionService.list()
                .stream()
                .collect(Collectors.toMap(UserPermissionEntity::getEmail, UserPermissionEntity::getPermissionGroupId));
            users.forEach(user -> user.setPermissionGroupId(permissionGroupNameMap.get(user.getEmail())));
            return users;
        } catch (RestClientException e) {
            log.error("Get nerko users error!", e);
            throw new EagleEyeException(OAUTH_CODE_ERROR);
        }
    }

    private List<UserPermissionGroupResponse> queryUser(String accessToken) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(accessToken);
            ResponseEntity<NerkoUserResponse> responseEntity = restTemplate
                .exchange(nerkoOAuth2Properties.getUsersEndpoint(),
                    HttpMethod.GET,
                    new HttpEntity<>(null, httpHeaders),
                    NerkoUserResponse.class);
            NerkoUserResponse nerkoUserResponse = Objects
                .requireNonNullElse(responseEntity.getBody(), new NerkoUserResponse());
            return nerkoUserResponse.getData();
        } catch (Unauthorized e) {
            log.warn("The token expires! refresh token!", e);
            usersAccessToken = getAccessTokenByGrantType(CLIENT_CREDENTIALS, null);
            return queryUser(usersAccessToken);
        }
    }


    private String getAccessTokenByGrantType(String grantType, String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(nerkoOAuth2Properties.getClientId(), nerkoOAuth2Properties.getClientSecret());
            MultiValueMap<String, String> param = createParams(grantType, code);
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(param, headers);
            OAuth2AccessTokenResponse response = restTemplate.postForObject(
                nerkoOAuth2Properties.getTokenEndpoint(), httpEntity, OAuth2AccessTokenResponse.class);
            assert response != null;
            return response.getAccessToken();
        } catch (HttpClientErrorException exception) {
            log.error(OAUTH_CODE_ERROR.getMessage(), exception);
            throw new EagleEyeException(OAUTH_CODE_ERROR);
        }
    }

    private MultiValueMap<String, String> createParams(String grantType, String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set(GRANT_TYPE, grantType);
        switch (grantType) {
            case AUTHORIZATION_CODE:
                params.set(CODE, code);
                break;
            case CLIENT_CREDENTIALS:
                params.set(SCOPE, SCOPE_VALUE);
                break;
            default:
                break;
        }
        return params;
    }

}