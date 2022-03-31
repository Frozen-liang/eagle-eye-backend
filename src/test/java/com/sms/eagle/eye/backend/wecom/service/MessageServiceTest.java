package com.sms.eagle.eye.backend.wecom.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;
import com.sms.eagle.eye.backend.wecom.service.impl.MessageServiceImpl;
import com.sms.eagle.eye.backend.wecom.utils.WeComResponseUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class MessageServiceTest {

    private static final MockedStatic<WeComResponseUtil> WE_COM_RESPONSE_UTIL_MOCKED_STATIC
        = mockStatic(WeComResponseUtil.class);

    @AfterAll
    public static void close() {
        WE_COM_RESPONSE_UTIL_MOCKED_STATIC.close();
    }


    private final WeComClient weComClient = mock(WeComClient.class);

    MessageService service = Mockito.spy(new MessageServiceImpl(weComClient));

    /**
     * {@link MessageServiceImpl#sendMessage(SendMessageRequest, String)}.
     *
     * <p>情况1：SendMessageRequest 不为空
     */
    @Test
    public void sendMessage_test1() {
        // 请求构建参数
        SendMessageRequest request = mock(SendMessageRequest.class);
        String applicationName = "applicationName";
        // mock weComClient.sendMessage
        SendMessageResponse response = mock(SendMessageResponse.class);
        when(weComClient.sendMessage(request, applicationName)).thenReturn(response);
        // mock static isSuccess
        WE_COM_RESPONSE_UTIL_MOCKED_STATIC.when(() -> WeComResponseUtil.isSuccess(response)).thenReturn(true);
        // invoke
        SendMessageResponse result = service.sendMessage(request, applicationName);
        // assertT
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(response);
    }

    /**
     * {@link MessageServiceImpl#sendMessage(SendMessageRequest, String)}.
     *
     * <p>情况2：SendMessageRequest 为空
     *
     */
    @Test
    public void sendMessage_test2() {
        // 请求构建参数
        SendMessageRequest request = null;
        String applicationName = "applicationName";
        // invoke
        SendMessageResponse result = service.sendMessage(request, applicationName);
        // assert
        assertThat(result).isNull();
    }
}
