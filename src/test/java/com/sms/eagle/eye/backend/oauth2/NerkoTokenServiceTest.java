package com.sms.eagle.eye.backend.oauth2;

import static com.sms.eagle.eye.backend.exception.ErrorCode.OAUTH_CODE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.OAuth2TokenResponse;
import com.sms.eagle.eye.backend.oauth2.impl.NerkoTokenServiceImpl;
import org.junit.jupiter.api.Test;
import sh.ory.hydra.ApiException;
import sh.ory.hydra.api.PublicApi;
import sh.ory.hydra.model.Oauth2TokenResponse;

public class NerkoTokenServiceTest {

    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String EXCEPTION_CODE = "code";

    private final PublicApi publicApi = mock(PublicApi.class);
    private final NerkoTokenServiceImpl tokenService = spy(new NerkoTokenServiceImpl(publicApi));

    /**
     * {@link NerkoTokenServiceImpl#getAccessTokenByCode(String)}
     *
     * <p>获取成功
     */
    @Test
    void getAccessTokenByCode_test_1() throws ApiException {
        String code = "code";
        // mock publicApi.oauth2Token()
        Oauth2TokenResponse response = mock(Oauth2TokenResponse.class);
        when(publicApi.oauth2Token(AUTHORIZATION_CODE, null, code, null, null))
            .thenReturn(response);
        // mock response.getAccessToken()
        String accessToken = "accessToken";
        when(response.getAccessToken()).thenReturn(accessToken);
        // invoke
        OAuth2TokenResponse result = tokenService.getAccessTokenByCode(code);
        // assert
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(accessToken);
    }

    /**
     * {@link NerkoTokenServiceImpl#getAccessTokenByCode(String)}
     *
     * <p>获取失败
     */
    @Test
    void getAccessTokenByCode_test_2() throws ApiException {
        String code = "code";
        // mock publicApi.oauth2Token()
        when(publicApi.oauth2Token(AUTHORIZATION_CODE, null, code, null, null))
            .thenThrow(new RuntimeException());
        // invoke and assert
        assertThatThrownBy(() -> tokenService.getAccessTokenByCode(code))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(OAUTH_CODE_ERROR.getCode());
    }

    /**
     * {@link NerkoTokenServiceImpl#getAccessTokenByClientCredential()}
     *
     * <p>获取成功
     */
    @Test
    void getAccessTokenByClientCredential_test_1() throws ApiException {
        // mock publicApi.oauth2Token()
        Oauth2TokenResponse response = mock(Oauth2TokenResponse.class);
        when(publicApi.oauth2Token(CLIENT_CREDENTIALS, null, null, null, null))
            .thenReturn(response);
        // mock response.getAccessToken()
        String accessToken = "accessToken";
        when(response.getAccessToken()).thenReturn(accessToken);
        // invoke
        String result = tokenService.getAccessTokenByClientCredential();
        // assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(accessToken);
    }

    /**
     * {@link NerkoTokenServiceImpl#getAccessTokenByClientCredential()}
     *
     * <p>获取失败
     */
    @Test
    void getAccessTokenByClientCredential_test_2() throws ApiException {
        // mock publicApi.oauth2Token()
        when(publicApi.oauth2Token(CLIENT_CREDENTIALS, null, null, null, null))
            .thenThrow(new RuntimeException());
        // invoke and assert
        assertThatThrownBy(() -> tokenService.getAccessTokenByClientCredential())
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(OAUTH_CODE_ERROR.getCode());
    }

    /**
     * {@link NerkoTokenServiceImpl#cleanClientCredentialTokenCache()}
     */
    @Test
    void cleanClientCredentialTokenCache() {
        tokenService.cleanClientCredentialTokenCache();
    }
}