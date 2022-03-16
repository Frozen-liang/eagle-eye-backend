package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.convert.PluginAlertFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.domain.service.impl.PluginAlertFieldServiceImpl;
import com.sms.eagle.eye.backend.response.plugin.AlarmLevelMappingResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginAlertRuleFieldResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PluginAlertFieldServiceTest {

    private final PluginAlertFieldConverter pluginAlertFieldConverter = mock(PluginAlertFieldConverter.class);
    private final PluginAlertFieldEntity pluginAlertFieldEntity = mock(PluginAlertFieldEntity.class);
    private final PluginAlertRuleFieldResponse ruleResponse = mock(PluginAlertRuleFieldResponse.class);
    private final PluginAlertFieldService pluginAlertFieldService =
            spy(new PluginAlertFieldServiceImpl(pluginAlertFieldConverter));

    private static final Long ID = 1L;

    @Test
    @DisplayName("Test the saveFromRpcData method in the Plugin Alert Field Service")
    public void saveFromRpcData_test() {
        List<ConfigField> list = mock(List.class);
        list.add(1, ConfigField.newBuilder().build());
        doReturn(true).when(pluginAlertFieldService).saveBatch(any());
        pluginAlertFieldService.saveFromRpcData(list,ID);
        verify(pluginAlertFieldService).saveBatch(any());
    }

    @Test
    @DisplayName("Test the getResponseByPluginId method in the Plugin Alert Field Service")
    public void getResponseByPluginId_test() {
        List<AlarmLevelMappingResponse> list = mock(List.class);
        doReturn(list).when(pluginAlertFieldService).list(any(Wrapper.class));
        assertThat(pluginAlertFieldService.getResponseByPluginId(ID)).hasSize(0);
    }

    @Test
    @DisplayName("Test the getListByPluginId method in the Plugin Alert Field Service")
    public void getListByPluginId_test() {
        List<PluginAlertFieldEntity> list = mock(List.class);
        doReturn(list).when(pluginAlertFieldService).list(any(Wrapper.class));
        assertThat(pluginAlertFieldService.getListByPluginId(ID)).isEqualTo(list);
    }
}
