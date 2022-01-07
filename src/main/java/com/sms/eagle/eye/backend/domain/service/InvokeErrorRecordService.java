package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.InvokeErrorRecordEntity;
import com.sms.eagle.eye.backend.request.alert.LambdaInvokeResult;

public interface InvokeErrorRecordService extends IService<InvokeErrorRecordEntity> {

    void addErrorRecord(LambdaInvokeResult request);
}
