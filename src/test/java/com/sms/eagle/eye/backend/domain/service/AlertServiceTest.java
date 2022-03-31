package com.sms.eagle.eye.backend.domain.service;

import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.alert.WebHookRequest;
import static org.assertj.core.api.Assertions.assertThat;
import org.checkerframework.checker.units.qual.N;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.domain.mapper.AlertMapper;
import com.sms.eagle.eye.backend.domain.service.impl.AlertServiceImpl;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

public class AlertServiceTest {

    AlertMapper alertMapper = mock(AlertMapper.class);
    AlertServiceImpl alertService = spy(new AlertServiceImpl());

    @Test
    void getPage_test() {
        IPage<AlertResponse> result = mock(IPage.class);
        Page<AlertEntity> page = mock(Page.class);
        AlertQueryRequest request = mock(AlertQueryRequest.class);
        when(request.getPageInfo()).thenReturn(page);
        doReturn(alertMapper).when(alertService).getBaseMapper();
        when(alertMapper.getPage(page, request)).thenReturn(result);
        assertThat(alertService.getPage(request)).isEqualTo(result);
    }

    @Test
    void saveAlert_test() {
//        // 构建请求对象
//        Integer alarmLevel = 1;
//        Long ID = 1L;
//        String PROJECT = "PROJECT";
//        String NAME = "NAME";
//        String ALARMLEVEL = "VAULE";
//        LocalDateTime ALERTTIME = LocalDateTime.now();
//        // mock
//        TaskEntity task = TaskEntity.builder().id(ID).project(PROJECT).name(NAME).build();
//        WebHookRequest webHookRequest = WebHookRequest.builder().alarmLevel(ALARMLEVEL).alertTime(ALERTTIME).build();
//        AlertEntity entity = AlertEntity.builder().alarmLevel(alarmLevel).build();
//        doReturn(Boolean.TRUE).when(alertService).save(entity);
//        // 执行
//        alertService.saveAlert(task, webHookRequest, alarmLevel);
//        // 验证
//        verify(alertService).save(entity);
    }

}