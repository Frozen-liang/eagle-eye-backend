package com.sms.eagle.eye.backend.nerko;

import com.sms.eagle.eye.backend.context.AccessTokenContextHolder;
import com.sms.eagle.eye.backend.nerko.interceptor.NerkoInterceptor;
import com.sms.eagle.eye.backend.oauth2.NerkoTokenService;
import feign.RequestTemplate;
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

public class NerkoInterceptorTest {

    private final NerkoInterceptor nerkoInterceptor = spy(new NerkoInterceptor());

    private static final MockedStatic<AccessTokenContextHolder> ACCESS_TOKEN_CONTEXT_HOLDER_MOCKED_STATIC
            = mockStatic(AccessTokenContextHolder.class);

    @AfterAll
    public static void close(){
        ACCESS_TOKEN_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    @Test
    public void apply_test(){
        // 构建请求参数
        String CREDENTIAL = "CREDENTIAL";
        String NAME = "NAME";
        String VALUE = "VALUE";
        // mock
        NerkoTokenService service = mock(NerkoTokenService.class);
        when(AccessTokenContextHolder.getTokenService()).thenReturn(service);
        when(service.getAccessTokenByClientCredential()).thenReturn(CREDENTIAL);
        RequestTemplate template = mock(RequestTemplate.class);
        doReturn(null).when(template).header(NAME,VALUE);
        // 执行
        nerkoInterceptor.apply(template);
        // 验证
        verify(template,times(1)).header(anyString(),anyString());
    }
}
