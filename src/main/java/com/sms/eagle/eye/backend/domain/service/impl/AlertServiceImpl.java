package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.mapper.AlertMapper;
import com.sms.eagle.eye.backend.domain.service.AlertService;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class AlertServiceImpl extends ServiceImpl<AlertMapper, AlertEntity>
    implements AlertService {

    @Override
    public IPage<AlertResponse> getPage(AlertQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Override
    public void saveAlert(TaskEntity task, WebHookRequest request) {
        save(AlertEntity.builder()
            .taskId(task.getId())
            .project(task.getProject())
            .taskName(task.getName())
            .description(request.getAlarmMessage())
            .utcAlertTime(request.getAlertTime())
            .build());
    }
}




