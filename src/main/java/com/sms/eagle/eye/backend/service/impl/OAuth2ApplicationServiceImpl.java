package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.config.NerkoOAuth2Configuration.ACCESS_TOKEN;
import static com.sms.eagle.eye.backend.exception.ErrorCode.OAUTH_CODE_ERROR;

import com.sms.eagle.eye.backend.config.NerkoOAuth2Properties;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.OAuth2AccessTokenResponse;
import com.sms.eagle.eye.backend.model.UserInfo;
import com.sms.eagle.eye.backend.response.UserResponse;
import com.sms.eagle.eye.backend.service.OAuth2ApplicationService;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OAuth2ApplicationServiceImpl implements OAuth2ApplicationService {

    private static final String GRANT_TYPE = "grant_type";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CODE = "code";

    private final RestTemplate restTemplate;
    private final NerkoOAuth2Properties nerkoOAuth2Properties;

    public OAuth2ApplicationServiceImpl(@Qualifier(ACCESS_TOKEN) RestTemplate restTemplate,
        NerkoOAuth2Properties nerkoOAuth2Properties) {
        this.restTemplate = restTemplate;
        this.nerkoOAuth2Properties = nerkoOAuth2Properties;
    }

    @Override
    public String getAccessToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.set(GRANT_TYPE, AUTHORIZATION_CODE);
            param.set(CODE, code);
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

    @Override
    public UserResponse getUserInfo() {
        UserInfo userInfo = SecurityUtil.getCurrentUser();
        return UserResponse.builder()
            .username(userInfo.getUsername())
            .email(userInfo.getEmail())
            .nickname(userInfo.getNickname())
            .build();
    }

}