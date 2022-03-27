package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.AlertListRequest;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import com.sms.eagle.eye.backend.service.AlertApplicationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监控预警.
 */
@Slf4j
@RestController
@RequestMapping("/v1/alert")
public class AlertController {

    private final AlertApplicationService alertApplicationService;

    public AlertController(AlertApplicationService alertApplicationService) {
        this.alertApplicationService = alertApplicationService;
    }

    @GetMapping("/page")
    public Response<CustomPage<AlertResponse>> page(AlertQueryRequest request) {
        return Response.ok(alertApplicationService.page(request));
    }

    @PostMapping("/webhook")
    public Response<Boolean> webhook(@RequestBody WebHookRequest request) {
        log.info("Alert webhook, {}", request);
        return Response.ok(alertApplicationService.resolveWebHook(request));
    }

    @GetMapping("/list")
    public Response<List<AlertResponse>> list(AlertListRequest request) {
        return Response.ok(alertApplicationService.list(request));
    }

}