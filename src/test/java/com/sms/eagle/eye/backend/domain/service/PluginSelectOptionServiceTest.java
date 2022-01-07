package com.sms.eagle.eye.backend.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.convert.PluginOptionConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginSelectOptionEntity;
import com.sms.eagle.eye.backend.domain.service.impl.PluginSelectOptionServiceImpl;
import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import com.sms.eagle.eye.plugin.v1.SelectOption;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class PluginSelectOptionServiceTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    PluginOptionConverter pluginOptionConverter = mock(PluginOptionConverter.class);
    PluginSelectOptionServiceImpl pluginSelectOptionService = spy(
        new PluginSelectOptionServiceImpl(objectMapper, pluginOptionConverter));

    @Test
    void getListByPluginId_test() {
        Long pluginId = 1L;
        List<PluginSelectOptionEntity> list = mock(List.class);
        doReturn(list).when(pluginSelectOptionService).list(any());
        assertThat(pluginSelectOptionService.getListByPluginId(pluginId)).isEqualTo(list);
    }

    @Test
    void getResponseByPluginId_test() throws JsonProcessingException {
        Long pluginId = 1L;
        String key = "Key";
        String data = "Data";
        List<PluginSelectOptionResponse.PluginSelectOptionItemResponse> dataList = mock(List.class);
        PluginSelectOptionEntity entity = mock(PluginSelectOptionEntity.class);
        List<PluginSelectOptionEntity> entityList = List.of(entity);
        doReturn(entityList).when(pluginSelectOptionService).getListByPluginId(pluginId);
        when(entity.getKey()).thenReturn(key);
        when(entity.getData()).thenReturn(data);
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenReturn(dataList);
        assertThat(pluginSelectOptionService.getResponseByPluginId(pluginId))
            .hasSize(1)
            .allMatch(response -> Objects.equals(response.getKey(), key)
                && Objects.equals(response.getData(), dataList));
    }

    @Test
    void saveFromRpcData_test() {
        Long pluginId = 1L;
        SelectOption selectOption = mock(SelectOption.class);
        List<SelectOption> rpcList = List.of(selectOption);
        PluginSelectOptionEntity entity = mock(PluginSelectOptionEntity.class);
        when(pluginOptionConverter.rpcToEntity(selectOption, pluginId, objectMapper)).thenReturn(entity);
        doReturn(true).when(pluginSelectOptionService).saveBatch(anyList());
        pluginSelectOptionService.saveFromRpcData(rpcList, pluginId);
        verify(pluginSelectOptionService).saveBatch(any());
    }

}