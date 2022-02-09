package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.InvokeErrorRecordEntity;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import java.util.List;

public interface InvokeErrorRecordService extends IService<InvokeErrorRecordEntity> {

    void addErrorRecord(Long taskId, String errorMsg);

    List<InvokeErrorRecordResponse> getErrorRecords(Long taskId);
}
