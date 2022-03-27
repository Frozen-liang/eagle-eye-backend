package com.sms.eagle.eye.backend.oauth2.impl;

import static com.sms.eagle.eye.backend.common.Constant.NERKO_TOKEN_CACHE_KEY;
import static com.sms.eagle.eye.backend.exception.ErrorCode.OAUTH_CODE_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.OAuth2TokenResponse;
import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import sh.ory.hydra.api.PublicApi;
import sh.ory.hydra.model.Oauth2TokenResponse;

@Slf4j
@Service
public class NerkoTokenServiceImpl implements NerkoTokenService {

    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private final PublicApi publicApi;

    public NerkoTokenServiceImpl(PublicApi publicApi) {
        this.publicApi = publicApi;
    }

    @Override
    public OAuth2TokenResponse getAccessTokenByCode(String code) {
        try {
            Oauth2TokenResponse response = publicApi
                .oauth2Token(AUTHORIZATION_CODE, null, code, null, null);
            return OAuth2TokenResponse.builder()
                .accessToken(response.getAccessToken())
                .idToken(response.getIdToken())
                .refreshToken(response.getRefreshToken())
                .build();
        } catch (Exception exception) {
            log.error(OAUTH_CODE_ERROR.getMessage(), exception);
            throw new EagleEyeException(OAUTH_CODE_ERROR);
        }
    }

    @Cacheable(value = NERKO_TOKEN_CACHE_KEY)
    @Override
    public String getAccessTokenByClientCredential() {
        try {
            log.info("Get Access Token");
            Oauth2TokenResponse response = publicApi
                .oauth2Token(CLIENT_CREDENTIALS, null, null, null, null);
            return response.getAccessToken();
        } catch (Exception exception) {
            log.error(OAUTH_CODE_ERROR.getMessage(), exception);
            throw new EagleEyeException(OAUTH_CODE_ERROR);
        }
    }

    @CacheEvict(value = NERKO_TOKEN_CACHE_KEY)
    @Override
    public void cleanClientCredentialTokenCache() {
        log.info("Evict Access Token");
    }
}