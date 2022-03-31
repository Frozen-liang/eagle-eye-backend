package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.response.task.PluginAlertRuleWithValueResponse;
import com.sms.eagle.eye.plugin.v1.ConfigField;
import com.sms.eagle.eye.plugin.v1.FieldType;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PluginAlertFieldConverterTest {

    private final PluginAlertFieldConverter converter = new PluginAlertFieldConverterImpl();
    private final ConfigField configField = mock(ConfigField.class);
    private final PluginAlertFieldEntity alertFieldEntity = mock(PluginAlertFieldEntity.class);

    private final Long PLUGINID = 1L;
    private final String KEY = "KEY";
    private final Object VAULE = Object.class;

    @Test
    void rpcToEntity_test1() {
        assertThat(converter.rpcToEntity(null, null)).isNull();
    }

    @Test
    void rpcToEntity_test2() {
        assertThat(converter.rpcToEntity(null, PLUGINID).getPluginId()).isEqualTo(PLUGINID);
    }

    @Test
    void rpcToEntity_test3() {
        // 执行
        PluginAlertFieldEntity entity = converter.rpcToEntity(configField, PLUGINID);
        // 验证
        assertThat(entity.getKey()).isNull();
        assertThat(entity.getLabelName()).isNull();
        assertThat(entity.getDefaultValue()).isNull();
        assertThat(entity.getPluginId()).isEqualTo(PLUGINID);
    }

    @Test
    void ToResponse_test1() {
        assertThat(converter.toResponse(null)).isNull();
    }

    @Test
    void ToResponse_test2() {
        String key = "key";
        when(alertFieldEntity.getKey()).thenReturn(key);
        assertThat(converter.toResponse(alertFieldEntity).getKey()).isEqualTo(key);
    }

    @Test
    void toValueResponse_test1() {
        assertThat(converter.toValueResponse(null, null)).isNull();
    }

    @Test
    void toValueResponse_test2() {
        when(alertFieldEntity.getKey()).thenReturn(KEY);
        PluginAlertRuleWithValueResponse response = converter.toValueResponse(alertFieldEntity, VAULE);
        assertThat(response.getKey()).isEqualTo(KEY);
        assertThat(response.getValue()).isEqualTo(VAULE);
    }

    @Test
    void map_test() {
        assertThat(converter.map(FieldType.INPUT)).isEqualTo(FieldType.INPUT_VALUE);
    }
}
