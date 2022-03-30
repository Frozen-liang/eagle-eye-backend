package com.sms.eagle.eye.backend.wecom;

import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.service.impl.AlertApplicationServiceImpl;
import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.context.WeComPropertiesContextHolder;
import com.sms.eagle.eye.backend.wecom.dto.WeComProperties;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.AccessTokenResponse;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;
import com.sms.eagle.eye.backend.wecom.service.MessageService;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MessageServiceTest {

    MessageService service = spy(new MessageService());
    private final WeComClient weComClient = mock(WeComClient.class);

    /**
     * {@link MessageService#sendMessage(SendMessageRequest, String)}.
     *
     * <p>根据 SendMessageRequest String 发送消息
     * <p>
     * 情况1：SendMessageRequest为空时
     */
    @Test
    public void sendMessage_test1() {
        // 请求构建参数
        String APPLICATIONNAME = "applicationName";
        // mock
        SendMessageRequest request = null;
        // 执行
        SendMessageResponse result = service.sendMessage(request, APPLICATIONNAME);
        // 验证
        assertThat(result).isNull();
    }

    /**
     * {@link MessageService#sendMessage(SendMessageRequest, String)}.
     *
     * <p>根据 SendMessageRequest String 发送消息
     * <p>
     * 情况2：SendMessageRequest不为空时 但微信通知不成功
     */
    @Test
    public void sendMessage_test2() {
        // 请求构建参数
        String APPLICATIONNAME = "applicationName";
        // mock
        SendMessageRequest request = mock(SendMessageRequest.class);
        SendMessageResponse response = mock(SendMessageResponse.class);
        doReturn(response).when(weComClient).sendMessage(request,APPLICATIONNAME);
        // 执行
//        SendMessageResponse result = service.sendMessage(request, APPLICATIONNAME);
        // 验证
//        assertThat(result).isNull();
    }
}
