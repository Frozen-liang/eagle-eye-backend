package com.sms.eagle.eye.backend.wecom.service;

import static com.sms.eagle.eye.backend.common.Constant.WECOM_TOKEN_CACHE_KEY;

import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 描述 访问令牌管理.
 **/
@Slf4j
@Service
public class TokenService extends AbstractBaseService {

    /**
     * 获取企业微信应用的access token.
     */
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
    @CacheEvict(value = WECOM_TOKEN_CACHE_KEY, key = "'wecom_access_token_'+#applicationName")
    public void clearAccessToken(String applicationName) {

    }
}
