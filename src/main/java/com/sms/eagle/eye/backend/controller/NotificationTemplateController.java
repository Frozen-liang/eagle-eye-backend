package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.request.template.NotificationTemplateRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateDetailResponse;
import com.sms.eagle.eye.backend.response.template.NotificationTemplateResponse;
import com.sms.eagle.eye.backend.service.NotificationTemplateApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息模版 模块.
 */
@Slf4j
@RestController
@RequestMapping("/v1/notification/template")
public class NotificationTemplateController {

    private final NotificationTemplateApplicationService notificationTemplateApplicationService;

    public NotificationTemplateController(
        NotificationTemplateApplicationService notificationTemplateApplicationService) {
        this.notificationTemplateApplicationService = notificationTemplateApplicationService;
    }

    /**
     * 根据 通道类型 和 模版类型 获取模版内容.
     */
    @GetMapping
    public Response<NotificationTemplateResponse> getTemplate(@RequestParam Integer channelType,
        @RequestParam Integer templateType) {
        return Response.ok(notificationTemplateApplicationService.getTemplate(channelType, templateType));
    }

    /**
     * 更新消息模版.
     */
    @PutMapping
    public Response<Boolean> updateTemplate(@RequestBody NotificationTemplateRequest request) {
        return Response.ok(notificationTemplateApplicationService.updateTemplate(request));
    }

    /**
     * 根据 channelType 获取告警通知的模版数据.
     */
    @GetMapping("/alert")
    public Response<NotificationTemplateDetailResponse> getAlertTemplate(@RequestParam Integer channelType) {
        return Response.ok(notificationTemplateApplicationService.getTemplateWithFieldInfo(
            channelType, NotificationTemplateType.ALERT));
    }
}