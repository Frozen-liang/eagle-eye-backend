package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.InvokeErrorRecordEntity;
import com.sms.eagle.eye.backend.domain.mapper.InvokeErrorRecordMapper;
import com.sms.eagle.eye.backend.domain.service.InvokeErrorRecordService;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class InvokeErrorRecordServiceImpl extends ServiceImpl<InvokeErrorRecordMapper, InvokeErrorRecordEntity>
    implements InvokeErrorRecordService {

    @Override
    public void addErrorRecord(Long taskId, String errorMsg) {
        save(InvokeErrorRecordEntity.builder()
            .taskId(taskId)
            .errMessage(errorMsg)
            .build());
    }

    @Override
    public List<InvokeErrorRecordResponse> getErrorRecords(Long taskId) {
        return getBaseMapper().getErrorRecords(taskId);
    }
}




