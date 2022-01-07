package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;

public interface AlertService extends IService<AlertEntity> {

    IPage<AlertResponse> getPage(AlertQueryRequest request);

    void saveAlert(TaskEntity task, WebHookRequest request);
}
