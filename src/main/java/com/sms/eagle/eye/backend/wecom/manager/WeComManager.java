package com.sms.eagle.eye.backend.wecom.manager;

import com.sms.eagle.eye.backend.wecom.service.MessageService;
import com.sms.eagle.eye.backend.wecom.service.TokenService;
import org.springframework.stereotype.Component;

@Component
public class WeComManager {

    private final TokenService tokenService;
    private final MessageService messageService;

    public WeComManager(TokenService tokenService,
        MessageService messageService) {
        this.tokenService = tokenService;
        this.messageService = messageService;
    }

    /**
     * 令牌管理服务.
     */
    public TokenService tokenService() {
        return tokenService;
    }

    /**
     * 消息管理.
     */
    public MessageService messageService() {
        return messageService;
    }

}