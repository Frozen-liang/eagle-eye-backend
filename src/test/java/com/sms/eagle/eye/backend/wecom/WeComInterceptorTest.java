package com.sms.eagle.eye.backend.wecom;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComContextHolder;
import com.sms.eagle.eye.backend.wecom.interceptor.WeComInterceptor;
import com.sms.eagle.eye.backend.wecom.manager.WeComManager;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import feign.RequestTemplate;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class WeComInterceptorTest {

    private final WeComInterceptor interceptor = spy(new WeComInterceptor());

    private final static MockedStatic<WeComContextHolder> WE_COM_CONTEXT_HOLDER_MOCKED_STATIC
            = mockStatic(WeComContextHolder.class);

    @AfterAll
    public static void close() {
        WE_COM_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    /**
     * {@link WeComInterceptor#apply(RequestTemplate)}.
     *
     * <p>根据参数RequestTemplate 统一处理AccessToken.
     * <p>
     * 情况2: headerApp不为空
     */
    @Test
    public void apply_test1() {
        // 请求构建参数
        String APP = "";
        String ACCESSTOKEN = "ACCESSTOKEN";
        String URL = "URL";
        String STRING = "STRING";
        int SIZE = 5;
        // mock
        RequestTemplate template = mock(RequestTemplate.class);
        when(template.url()).thenReturn(URL);
        Map<String, Collection<String>> map = mock(Map.class);
        when(template.headers()).thenReturn(map);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            strings.add(STRING);
        }
        when(map.get(WeComClient.HEAD_KEY)).thenReturn(strings);
        WeComManager weComManager = mock(WeComManager.class);
        WE_COM_CONTEXT_HOLDER_MOCKED_STATIC.when(WeComContextHolder::getWeChatManager).thenReturn(weComManager);
        TokenService service = mock(TokenService.class);
        doReturn(service).when(weComManager).tokenService();
        doReturn(ACCESSTOKEN).when(service).getAccessToken(anyString());

        when(template.query(WeComClient.ACCESS_TOKEN, ACCESSTOKEN)).thenReturn(template);
        // 执行
        interceptor.apply(template);
        // 验证
        verify(template, times(1)).query(WeComClient.ACCESS_TOKEN, ACCESSTOKEN);
    }

    /**
     * {@link WeComInterceptor#apply(RequestTemplate)}.
     *
     * <p>根据参数RequestTemplate 统一处理AccessToken.
     * <p>
     * 情况1: headerApp为空
     */
    @Test
    public void apply_test2() {
        // 请求构建参数
        String APP = "";
        String ACCESSTOKEN = "ACCESSTOKEN";
        String URL = "URL";
        // mock
        RequestTemplate template = mock(RequestTemplate.class);
        when(template.url()).thenReturn(URL);
        WeComManager weComManager = mock(WeComManager.class);
        WE_COM_CONTEXT_HOLDER_MOCKED_STATIC.when(WeComContextHolder::getWeChatManager).thenReturn(weComManager);
        TokenService service = mock(TokenService.class);
        when(weComManager.tokenService()).thenReturn(service);
        when(service.getAccessToken(APP)).thenReturn(ACCESSTOKEN);
        when(template.query(any(), anyString())).thenReturn(template);
        // 验证
        assertThatThrownBy(() -> interceptor.apply(template))
                .isInstanceOf(RuntimeException.class);
    }
}
