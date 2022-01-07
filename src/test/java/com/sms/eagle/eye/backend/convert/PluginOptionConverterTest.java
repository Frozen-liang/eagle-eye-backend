package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.entity.PluginSelectOptionEntity;
import com.sms.eagle.eye.plugin.v1.SelectOption;
import org.junit.jupiter.api.Test;

public class PluginOptionConverterTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    PluginOptionConverterImpl converter = spy(new PluginOptionConverterImpl());

    @Test
    void rpcToEntity_Test1() {
        assertThat(converter.rpcToEntity(null, null, objectMapper)).isNull();
    }

    @Test
    void rpcToEntity_Test2() throws JsonProcessingException {
        String value = "value";
        String key = "key";
        Long pluginId = 1L;
        when(objectMapper.writeValueAsString(any())).thenReturn(value);

        SelectOption pluginSelectOption = mock(SelectOption.class);
        when(pluginSelectOption.getKey()).thenReturn(key);
        PluginSelectOptionEntity entity = converter.rpcToEntity(pluginSelectOption, pluginId, objectMapper);
        assertThat(entity.getKey()).isEqualTo(key);
        assertThat(entity.getData()).isEqualTo(value);
        assertThat(entity.getPluginId()).isEqualTo(pluginId);
    }
}