package com.sms.eagle.eye.backend.domain.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.sms.eagle.eye.backend.domain.entity.InvokeErrorRecordEntity;
import com.sms.eagle.eye.backend.domain.service.impl.InvokeErrorRecordServiceImpl;
import org.junit.jupiter.api.Test;

public class InvokeErrorRecordServiceTest {

    InvokeErrorRecordService invokeErrorRecordService = spy(new InvokeErrorRecordServiceImpl());

    @Test
    void addErrorRecord_test() {
        doReturn(Boolean.TRUE).when(invokeErrorRecordService).save(any(InvokeErrorRecordEntity.class));
        Long taskId = 1L;
        String errorMsg = "err";
        invokeErrorRecordService.addErrorRecord(taskId, errorMsg);
        verify(invokeErrorRecordService).save(any());
    }
}