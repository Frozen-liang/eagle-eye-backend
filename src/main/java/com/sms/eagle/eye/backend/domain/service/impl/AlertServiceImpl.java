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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class AlertServiceImpl extends ServiceImpl<AlertMapper, AlertEntity>
    implements AlertService {

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public IPage<AlertResponse> getPage(AlertQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Override
    public void saveAlert(TaskEntity task, WebHookRequest request, Integer alarmLevel) {
        save(AlertEntity.builder()
            .taskId(task.getId())
            .project(task.getProject())
            .taskName(task.getName())
            .alarmLevel(alarmLevel)
            .description(request.getAlarmMessage())
            .utcAlertTime(request.getAlertTime())
            .build());
    }

    @Override
    public List<AlertResponse> getResponseList(LocalDate from, LocalDate to) {
        return getBaseMapper().getResponseList(
            from.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
            to.format(DateTimeFormatter.ofPattern(DATE_FORMAT)),
            DATE_FORMAT);
    }
}




