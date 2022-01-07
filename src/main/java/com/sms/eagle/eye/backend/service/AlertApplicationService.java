package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;

public interface AlertApplicationService {

    CustomPage<AlertResponse> page(AlertQueryRequest request);

    boolean resolveWebHook(WebHookRequest request);
}
