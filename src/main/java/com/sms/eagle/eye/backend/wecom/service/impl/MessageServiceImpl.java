package com.sms.eagle.eye.backend.wecom.service.impl;

import static com.sms.eagle.eye.backend.wecom.utils.WeComResponseUtil.isSuccess;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;
import com.sms.eagle.eye.backend.wecom.service.MessageService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final WeComClient weComClient;

    public MessageServiceImpl(WeComClient weComClient) {
        this.weComClient = weComClient;
    }

    @Override
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