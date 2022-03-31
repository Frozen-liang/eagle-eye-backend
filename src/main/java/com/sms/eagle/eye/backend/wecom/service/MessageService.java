package com.sms.eagle.eye.backend.wecom.service;


import com.sms.eagle.eye.backend.wecom.request.SendMessageRequest;
import com.sms.eagle.eye.backend.wecom.response.SendMessageResponse;

/**
 * 消息管理.
 **/
public interface MessageService {

    /**
     * 发送消息.
     */
    SendMessageResponse sendMessage(SendMessageRequest request, String applicationName);

}
