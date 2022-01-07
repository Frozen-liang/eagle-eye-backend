package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.domain.mapper.AlertMapper;
import com.sms.eagle.eye.backend.domain.service.impl.AlertServiceImpl;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import org.junit.jupiter.api.Test;

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

}