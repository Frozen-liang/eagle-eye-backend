package com.sms.eagle.eye.backend.oauth2;

import com.sms.eagle.eye.backend.model.OAuth2TokenResponse;

public interface NerkoTokenService {

    OAuth2TokenResponse getAccessTokenByCode(String code);

    String getAccessTokenByClientCredential();

    void cleanClientCredentialTokenCache();
}
