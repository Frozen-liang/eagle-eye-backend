package com.sms.eagle.eye.backend.convert;

import com.sms.eagle.eye.backend.domain.entity.PluginAlertFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigMetadataConverterTest {

    private final ConfigMetadataConverterImpl converter = new ConfigMetadataConverterImpl();
    private final ChannelFieldResponse response = mock(ChannelFieldResponse.class);
    private final PluginConfigFieldEntity configFieldEntity = mock(PluginConfigFieldEntity.class);
    private final PluginAlertFieldEntity alertFieldEntity = mock(PluginAlertFieldEntity.class);

    private final String KEY = "KEY";

    @Test
    void fromChannelField_test1() {
        assertThat(converter.fromChannelField(null)).isNull();
    }

    @Test
    void fromChannelField_test2() {
        // mock
        when(response.getKey()).thenReturn(KEY);
        // 执行
        ConfigMetadata configMetadata = converter.fromChannelField(response);
        // 验证
        assertThat(configMetadata).isNotNull();
        assertThat(configMetadata.getKey()).isEqualTo(KEY);
    }

    @Test
    void fromConfigField_test1() {
        assertThat(converter.fromConfigField(null)).isNull();
    }

    @Test
    void fromConfigField_test2() {
        // mock
        when(configFieldEntity.getKey()).thenReturn(KEY);
        // 执行
        ConfigMetadata configMetadata = converter.fromConfigField(configFieldEntity);
        // 验证
        assertThat(configMetadata).isNotNull();
        assertThat(configMetadata.getKey()).isEqualTo(KEY);
    }

    @Test
    void fromAlertField_test1() {
        assertThat(converter.fromAlertField(null)).isNull();
    }

    @Test
    void fromAlertField_test2() {
        // mock
        when(alertFieldEntity.getKey()).thenReturn(KEY);
        // 执行
        ConfigMetadata configMetadata = converter.fromAlertField(alertFieldEntity);
        // 验证
        assertThat(configMetadata).isNotNull();
        assertThat(configMetadata.getKey()).isEqualTo(KEY);
    }
}
