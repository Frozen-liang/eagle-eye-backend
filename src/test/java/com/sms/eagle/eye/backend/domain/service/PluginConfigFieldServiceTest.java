package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.service.impl.PluginConfigFieldServiceImpl;
import com.sms.eagle.eye.backend.response.plugin.PluginConfigFieldResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class PluginConfigFieldServiceTest {

    PluginConfigFieldConverter pluginConfigFieldConverter = mock(PluginConfigFieldConverter.class);
    PluginConfigFieldServiceImpl pluginConfigFieldService = spy(
        new PluginConfigFieldServiceImpl(pluginConfigFieldConverter));

    @Test
    void getListByPluginId_test() {
        Long pluginId = 1L;
        List<PluginConfigFieldEntity> list = mock(List.class);
        doReturn(list).when(pluginConfigFieldService).list(any(Wrapper.class));
        assertThat(pluginConfigFieldService.getListByPluginId(pluginId)).isEqualTo(list);
    }

    @Test
    void getResponseByPluginId_test() {
        Long pluginId = 1L;
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        List<PluginConfigFieldEntity> entityList = new ArrayList();
        entityList.add(entity);
        PluginConfigFieldResponse response = mock(PluginConfigFieldResponse.class);
        doReturn(entityList).when(pluginConfigFieldService).getListByPluginId(pluginId);
        when(pluginConfigFieldConverter.toResponse(any(PluginConfigFieldEntity.class))).thenReturn(response);
        assertThat(pluginConfigFieldService.getResponseByPluginId(pluginId)).hasSize(1)
            .allMatch(pluginConfigFieldResponse -> Objects.equals(pluginConfigFieldResponse, response));
    }

    @Test
    void saveFromRpcData_test() {
        Long pluginId = 1L;
        ConfigField configField = mock(ConfigField.class);
        List<ConfigField> configFields = List.of(configField);
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(pluginConfigFieldConverter.rpcToEntity(configField, pluginId)).thenReturn(entity);
        doReturn(true).when(pluginConfigFieldService).saveBatch(anyList(), anyInt());
        pluginConfigFieldService.saveFromRpcData(configFields, pluginId);
        verify(pluginConfigFieldService).saveBatch(any());
    }

}