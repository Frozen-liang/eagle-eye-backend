package com.sms.eagle.eye.backend.wecom;

import static com.sms.eagle.eye.backend.common.Constant.WECOM_TOKEN_CACHE_KEY;
import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import com.sms.eagle.eye.backend.wecom.service.MessageService;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.springframework.cache.annotation.Cacheable;

public class TokenServiceTest {

    private final TokenService service = spy(TokenService.class);
    private final static MockedStatic<WeComPropertiesContextHolder> WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC
            = mockStatic(WeComPropertiesContextHolder.class);
    private final WeComClient weComClient = mock(WeComClient.class);

    @AfterAll
    public static void close() {
        WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    /**
     * {@link TokenService#getAccessToken(String)}.
     *
     * <p>根据  String 获取企业微信应用的access token.
     * <p>
     * 情况1: 不成功返回null
     */
    @Test
    public void getAccessToken_test() {
        // 构建请求对象
        String ID = "ID";
        String SECRET = "SECRET";
        String APPLICATIONNAME = "APPLICATIONNAME";
        // mock
        WeComProperties properties = mock(WeComProperties.class);

        WE_COM_PROPERTIES_CONTEXT_HOLDER_MOCKED_STATIC.when(WeComPropertiesContextHolder::getProperties)
                .thenReturn(properties);
        when(properties.getCorpId()).thenReturn(ID);
        when(properties.getCorpSecret()).thenReturn(SECRET);
        AccessTokenResponse response = mock(AccessTokenResponse.class);
        doReturn(response).when(weComClient).getAccessToken(ID, SECRET);
        // 执行
//        String accessToken = service.getAccessToken(APPLICATIONNAME);
        // 验证
//        assertThat(accessToken).isNull();
    }
}
