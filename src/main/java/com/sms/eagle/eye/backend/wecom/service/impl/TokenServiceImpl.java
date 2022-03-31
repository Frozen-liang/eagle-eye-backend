package com.sms.eagle.eye.backend.wecom.service.impl;

import static com.sms.eagle.eye.backend.common.Constant.WECOM_TOKEN_CACHE_KEY;
import static com.sms.eagle.eye.backend.wecom.utils.WeComResponseUtil.isSuccess;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final WeComClient weComClient;

    public TokenServiceImpl(WeComClient weComClient) {
        this.weComClient = weComClient;
    }

    /**
     * 获取企业微信应用的access token.
     */
    @Override
    @Cacheable(value = WECOM_TOKEN_CACHE_KEY, key = "'wecom_access_token_'+#applicationName")
    public String getAccessToken(String applicationName) {
        String accessToken = null;
        WeComProperties properties = WeComPropertiesContextHolder.getProperties();
        AccessTokenResponse accessTokenResponse = weComClient.getAccessToken(
            properties.getCorpId(), properties.getCorpSecret());
        if (isSuccess(accessTokenResponse)) {
            accessToken = accessTokenResponse.getAccessToken();
        }
        return accessToken;
    }

    /**
     * 清除应用AccessToken缓存.
     */
    @Override
    @CacheEvict(value = WECOM_TOKEN_CACHE_KEY, key = "'wecom_access_token_'+#applicationName")
    public void clearAccessToken(String applicationName) {

    }
}