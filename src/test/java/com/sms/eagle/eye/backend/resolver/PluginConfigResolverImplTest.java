package com.sms.eagle.eye.backend.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.resolver.impl.PluginConfigResolverImpl;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;


public class PluginConfigResolverImplTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    StringEncryptor stringEncryptor = mock(StringEncryptor.class);
    PluginConfigFieldConverter pluginConfigFieldConverter = mock(PluginConfigFieldConverter.class);

    PluginConfigResolverImpl pluginConfigResolver = spy(new PluginConfigResolverImpl(
        objectMapper, stringEncryptor, pluginConfigFieldConverter));

    @Test
    void checkAndEncrypt_test1() throws JsonProcessingException {
        String config = "{}";
        String key = "key";
        when(objectMapper.readValue(config, Map.class)).thenReturn(new HashMap());
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(entity.getKey()).thenReturn(key);
        when(entity.getRequired()).thenReturn(Boolean.TRUE);
        List<PluginConfigFieldEntity> fields = List.of(entity);
        assertThatThrownBy(() -> pluginConfigResolver.checkAndEncrypt(fields, config))
            .isInstanceOf(EagleEyeException.class);
    }

    @Test
    void checkAndEncrypt_test2() throws JsonProcessingException {
        String config = "{}";
        String key = "key";
        String value = "value";
        Map<String, String> map = mock(HashMap.class);
        when(map.get(key)).thenReturn(value);
        when(objectMapper.readValue(config, Map.class)).thenReturn(map);
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(entity.getKey()).thenReturn(key);
        when(entity.getEncrypted()).thenReturn(Boolean.TRUE);
        String newVal = "123";
        when(stringEncryptor.encrypt(value)).thenReturn(newVal);
        String result = "{}";
        when(objectMapper.writeValueAsString(map)).thenReturn(result);
        assertThat(pluginConfigResolver.checkAndEncrypt(List.of(entity), config)).isEqualTo(result);
    }

    @Test
    void checkAndEncrypt_test3() throws JsonProcessingException {
        String config = "{}";
        String key = "key";
        String value = "value";
        Map<String, String> map = mock(HashMap.class);
        when(map.get(key)).thenReturn(value);
        when(objectMapper.readValue(config, Map.class)).thenReturn(map);
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        when(entity.getKey()).thenReturn(key);
        when(entity.getEncrypted()).thenReturn(Boolean.FALSE);
        String newVal = "123";
        when(stringEncryptor.encrypt(value)).thenReturn(newVal);
        String result = "{}";
        when(objectMapper.writeValueAsString(map)).thenReturn(result);
        assertThat(pluginConfigResolver.checkAndEncrypt(List.of(entity), config)).isEqualTo(result);
    }

    @Test
    void decrypt_test1() throws JsonProcessingException {
        String config = "config";
        PluginConfigFieldEntity entity = mock(PluginConfigFieldEntity.class);
        Map<String, String> map = mock(HashMap.class);
        when(objectMapper.readValue(config, Map.class)).thenReturn(map);
        String key = "key";
        when(entity.getKey()).thenReturn(key);
        String value = "value";
        when(map.get(key)).thenReturn(value);
        PluginConfigFieldWithValueResponse response = mock(PluginConfigFieldWithValueResponse.class);
        when(pluginConfigFieldConverter.toValueResponse(entity, value)).thenReturn(response);
        when(response.getValue()).thenReturn(value);
        when(entity.getEncrypted()).thenReturn(Boolean.TRUE);
        assertThat(pluginConfigResolver.decryptToResponse(List.of(entity), config))
            .hasSize(1)
            .allMatch(item -> Objects.equals(item, response));
    }


}