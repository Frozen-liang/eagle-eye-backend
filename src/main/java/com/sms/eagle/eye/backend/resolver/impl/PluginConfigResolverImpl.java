package com.sms.eagle.eye.backend.resolver.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_CONFIG_FIELD_MISSING_ERROR;
import static com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse.DEFAULT_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class PluginConfigResolverImpl implements PluginConfigResolver {

    private static final String DEFAULT_MAP = "{}";

    private final ObjectMapper objectMapper;
    private final StringEncryptor stringEncryptor;
    private final PluginConfigFieldConverter pluginConfigFieldConverter;

    public PluginConfigResolverImpl(ObjectMapper objectMapper, StringEncryptor stringEncryptor,
        PluginConfigFieldConverter pluginConfigFieldConverter) {
        this.objectMapper = objectMapper;
        this.stringEncryptor = stringEncryptor;
        this.pluginConfigFieldConverter = pluginConfigFieldConverter;
    }

    @Override
    public String checkAndEncrypt(List<PluginConfigFieldEntity> fields, String config) {
        Map<String, String> map = convertConfigToMap(config);
        fields.forEach(pluginConfigField -> {
            String value = map.get(pluginConfigField.getKey());
            if (StringUtils.isBlank(value)) {
                requiredCheck(pluginConfigField);
            } else {
                getEncryptValueIfNeed(value, pluginConfigField).ifPresent(newVal ->
                    map.put(pluginConfigField.getKey(), newVal));
            }
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    @Override
    public List<PluginConfigFieldWithValueResponse> decryptToResponse(List<PluginConfigFieldEntity> fields,
        String config) {
        Map<String, String> map = convertConfigToMap(config);
        return fields.stream().map(pluginConfigField -> {
            String value = map.get(pluginConfigField.getKey());
            PluginConfigFieldWithValueResponse response =
                pluginConfigFieldConverter.toValueResponse(pluginConfigField, value);
            decryptIfNeed(response, pluginConfigField);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public String decryptToString(List<PluginConfigFieldEntity> fields, String config) {
        Map<String, String> map = convertConfigToMap(config);
        fields.forEach(pluginConfigField -> {
            String value = map.get(pluginConfigField.getKey());
            if (StringUtils.isBlank(value)) {
                requiredCheck(pluginConfigField);
            } else {
                getDecryptValueIfNeed(value, pluginConfigField).ifPresent(newVal ->
                    map.put(pluginConfigField.getKey(), newVal));
            }
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    private Map<String, String> convertConfigToMap(String config) {
        return Try.of(() -> objectMapper.readValue(config, Map.class)).getOrElse(new HashMap<>());
    }

    private void requiredCheck(PluginConfigFieldEntity configField) {
        if (Objects.equals(configField.getRequired(), Boolean.TRUE)) {
            throw new EagleEyeException(PLUGIN_CONFIG_FIELD_MISSING_ERROR, configField.getLabelName());
        }
    }

    private Optional<String> getEncryptValueIfNeed(String oldValue, PluginConfigFieldEntity configField) {
        if (Objects.equals(configField.getEncrypted(), Boolean.TRUE)) {
            return Optional.of(stringEncryptor.encrypt(oldValue));
        } else {
            return Optional.empty();
        }
    }

    private Optional<String> getDecryptValueIfNeed(String oldValue, PluginConfigFieldEntity configField) {
        if (Objects.equals(configField.getEncrypted(), Boolean.TRUE)) {
            return Optional.of(stringEncryptor.decrypt(oldValue));
        } else {
            return Optional.empty();
        }
    }

    private void decryptIfNeed(PluginConfigFieldWithValueResponse response, PluginConfigFieldEntity configField) {
        if (StringUtils.isNotBlank(response.getValue())
            && Objects.equals(configField.getEncrypted(), Boolean.TRUE)) {
            response.setValue(DEFAULT_VALUE);
        }
    }
}