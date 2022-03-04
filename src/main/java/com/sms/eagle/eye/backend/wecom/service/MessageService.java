package com.sms.eagle.eye.backend.wecom.service;


import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 描述 消息管理.
 **/
@Slf4j
@Service
public class MessageService extends AbstractBaseService {

    /**
     * 发送消息.
     */
    public final SendMessageResponse sendMessage(SendMessageRequest request, String applicationName) {
        SendMessageResponse response = null;
        if (Objects.nonNull(request)) {
            response = weComClient.sendMessage(request, applicationName);
            if (isSuccess(response)) {
                log.info("企业微信通知成功，{}", request.getToUser());
            }
        }
        return response;
    }

}
