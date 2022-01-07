package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import com.sms.eagle.eye.plugin.v1.FieldType;
import org.junit.jupiter.api.Test;

public class PluginConfigFieldConverterTest {

    PluginConfigFieldConverterImpl converter = new PluginConfigFieldConverterImpl();

    @Test
    void rpcToEntity_test1() {
        assertThat(converter.rpcToEntity(null, null)).isNull();
    }

    @Test
    void rpcToEntity_test2() {
        Long pluginId = 1L;
        assertThat(converter.rpcToEntity(null, pluginId).getPluginId()).isEqualTo(pluginId);
    }

    @Test
    void rpcToEntity_test3() {
        Long pluginId = 1L;
        ConfigField configField = mock(ConfigField.class);
        PluginConfigFieldEntity entity = converter.rpcToEntity(configField, pluginId);
        assertThat(entity.getKey()).isNull();
        assertThat(entity.getLabelName()).isNull();
        assertThat(entity.getDefaultValue()).isNull();
        assertThat(entity.getPluginId()).isEqualTo(pluginId);
    }

    @Test
    void rpcToResponse_test1() {
        assertThat(converter.rpcToResponse(null)).isNull();
    }

    @Test
    void rpcToResponse_test2() {
        String key = "key";
        ConfigField configField = mock(ConfigField.class);
        when(configField.getKey()).thenReturn(key);
        assertThat(converter.rpcToResponse(configField).getKey()).isEqualTo(key);
    }

    @Test
    void toResponse_test1() {
        assertThat(converter.toResponse(null)).isNull();
    }

    @Test
    void toResponse_test2() {
        String key = "key";
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(entity.getKey()).thenReturn(key);
        assertThat(converter.toResponse(entity).getKey()).isEqualTo(key);
    }

    @Test
    void toValueResponse_test1() {
        assertThat(converter.toValueResponse(null, null)).isNull();
    }

    @Test
    void toValueResponse_test2() {
        String value = "value";
        String key = "key";
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(entity.getKey()).thenReturn(key);
        PluginConfigFieldWithValueResponse response = converter.toValueResponse(entity, value);
        assertThat(response.getKey()).isEqualTo(key);
        assertThat(response.getValue()).isEqualTo(value);
    }

    @Test
    void map_test() {
        assertThat(converter.map(FieldType.INPUT)).isEqualTo(FieldType.INPUT_VALUE);
    }

}