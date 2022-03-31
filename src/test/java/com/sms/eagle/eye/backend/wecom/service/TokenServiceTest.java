package com.sms.eagle.eye.backend.wecom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import com.sms.eagle.eye.backend.wecom.service.impl.TokenServiceImpl;
import com.sms.eagle.eye.backend.wecom.utils.WeComResponseUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class TokenServiceTest {

    private static final MockedStatic<WeComResponseUtil> WE_COM_RESPONSE_UTIL_MOCKED_STATIC
        = mockStatic(WeComResponseUtil.class);
    private final static MockedStatic<WeComPropertiesContextHolder> WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC
        = mockStatic(WeComPropertiesContextHolder.class);

    @AfterAll
    public static void close() {
        WE_COM_RESPONSE_UTIL_MOCKED_STATIC.close();
        WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    private final WeComClient weComClient = mock(WeComClient.class);
    private final TokenService service = Mockito.spy(new TokenServiceImpl(weComClient));


    /**
     * {@link TokenServiceImpl#getAccessToken(String)}}
     *
     * 情况1: 不成功返回null
     */
    @Test
    public void getAccessToken_test() {
        // request
        String applicationName = "applicationName";
        //
        WeComProperties properties = mock(WeComProperties.class);
        WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC.when(() -> WeComPropertiesContextHolder.getProperties())
            .thenReturn(properties);
        // mock properties.getCorpId()
        String corpId = "corpId";
        when(properties.getCorpId()).thenReturn(corpId);
        // mock properties.getCorpSecret()
        String corpSecret = "corpSecret";
        when(properties.getCorpSecret()).thenReturn(corpSecret);
        // mock weComClient.getAccessToken
        AccessTokenResponse accessTokenResponse = mock(AccessTokenResponse.class);
        when(weComClient.getAccessToken(corpId, corpSecret)).thenReturn(accessTokenResponse);
        // mock static isSuccess()
        WE_COM_RESPONSE_UTIL_MOCKED_STATIC.when(() -> WeComResponseUtil.isSuccess(accessTokenResponse))
            .thenReturn(false);
        // invoke
        String result = service.getAccessToken(applicationName);
        // assert
        assertThat(result).isNull();
    }

    /**
     * {@link TokenServiceImpl#getAccessToken(String)}}
     *
     * 情况2: 成功返回accessToken
     */
    @Test
    public void getAccessToken_test_2() {
        // request
        String applicationName = "applicationName";
        //
        WeComProperties properties = mock(WeComProperties.class);
        WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC.when(() -> WeComPropertiesContextHolder.getProperties())
            .thenReturn(properties);
        // mock properties.getCorpId()
        String corpId = "corpId";
        when(properties.getCorpId()).thenReturn(corpId);
        // mock properties.getCorpSecret()
        String corpSecret = "corpSecret";
        when(properties.getCorpSecret()).thenReturn(corpSecret);
        // mock weComClient.getAccessToken
        AccessTokenResponse accessTokenResponse = mock(AccessTokenResponse.class);
        when(weComClient.getAccessToken(corpId, corpSecret)).thenReturn(accessTokenResponse);
        // mock static isSuccess()
        WE_COM_RESPONSE_UTIL_MOCKED_STATIC.when(() -> WeComResponseUtil.isSuccess(accessTokenResponse))
            .thenReturn(true);
        // mock accessTokenResponse.getAccessToken()
        String accessToken = "accessToken";
        when(accessTokenResponse.getAccessToken()).thenReturn(accessToken);
        // invoke
        String result = service.getAccessToken(applicationName);
        // assert
        assertThat(result).isEqualTo(accessToken);
    }

    /**
     * {@link TokenServiceImpl#clearAccessToken(String)}}
     */
    @Test
    void clearAccessToken_test() {
        String applicationName = "applicationName";
        service.clearAccessToken(applicationName);
    }
}
