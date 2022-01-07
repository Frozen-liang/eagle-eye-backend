package com.sms.eagle.eye.backend.convert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import org.junit.jupiter.api.Test;

public class PluginConverterTest {

    PluginConverterImpl pluginConverter = spy(new PluginConverterImpl());

    @Test
    void rpcResponseToEntity_test1() {
        assertThat(pluginConverter.rpcResponseToEntity(null, null, null)).isNull();
    }

    @Test
    void rpcResponseToEntity_test2() {
        String name = "name";
        String url = "url";
        String creator = "creator";
        RegisterResponse response = mock(RegisterResponse.class);
        when(response.getName()).thenReturn(name);
        PluginEntity entity = pluginConverter.rpcResponseToEntity(response, url, creator);
        assertThat(entity.getName()).isEqualTo(name);
        assertThat(entity.getUrl()).isEqualTo(url);
        assertThat(entity.getCreator()).isEqualTo(creator);
    }
}