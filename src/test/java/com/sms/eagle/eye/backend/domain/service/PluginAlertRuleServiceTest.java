package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.service.impl.PluginAlertRuleServiceImpl;
import com.sms.eagle.eye.backend.request.plugin.AlertRuleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PluginAlertRuleServiceTest {

    private final PluginAlertRuleService pluginAlertRuleService = spy(new PluginAlertRuleServiceImpl());

    private final PluginAlertRuleEntity pluginAlertRuleEntity = mock(PluginAlertRuleEntity.class);
    private final AlertRuleRequest ruleRequest = mock(AlertRuleRequest.class);

    private static final Integer INTEGER = 1;
    private static final Long ID = 1L;

    @Test
    @DisplayName("Test the updateByRequest method in the PluginAlertRuleService")
    public void updateByRequest_test() {
        doReturn(true).when(pluginAlertRuleService).remove(any());
        when(ruleRequest.getAlertKeys()).thenReturn(Collections.emptyList());
        when(ruleRequest.getAlarmLevel()).thenReturn(INTEGER);
        doReturn(true).when(pluginAlertRuleService).saveBatch(any());
        pluginAlertRuleService.updateByRequest(List.of(ruleRequest), ID);
        verify(pluginAlertRuleService).remove(any());
        verify(pluginAlertRuleService).saveBatch(any());
    }

    @Test
    @DisplayName("Test the getListByPluginId method in the PluginAlertRuleService")
    public void getListByPluginId_test() {
        List<PluginAlertRuleEntity> list = mock(List.class);
        doReturn(list).when(pluginAlertRuleService).list(any(Wrapper.class));
        assertThat(pluginAlertRuleService.getListByPluginId(ID)).isEqualTo(list);
    }
}
