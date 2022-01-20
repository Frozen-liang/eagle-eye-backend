package com.sms.eagle.eye.backend.resolver.impl;

import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_KEY_GROUP;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_PATTERN;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_REGEX;
import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_CONFIG_FIELD_MISSING_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.convert.PluginConfigFieldConverter;
import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.resolver.PluginConfigResolver;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PluginConfigResolverImpl implements PluginConfigResolver {

    private static final String DEFAULT_MAP = "{}";
    private static final String SENSITIVE_FLAG = "[EAGLE-ENCRYPTED-VALUE]";

    private final ObjectMapper objectMapper;
    private final StringEncryptor stringEncryptor;
    private final PluginConfigFieldConverter pluginConfigFieldConverter;
    private final PasswordStoreService passwordStoreService;

    public PluginConfigResolverImpl(ObjectMapper objectMapper, StringEncryptor stringEncryptor,
        PluginConfigFieldConverter pluginConfigFieldConverter,
        PasswordStoreService passwordStoreService) {
        this.objectMapper = objectMapper;
        this.stringEncryptor = stringEncryptor;
        this.pluginConfigFieldConverter = pluginConfigFieldConverter;
        this.passwordStoreService = passwordStoreService;
    }

    /**
     * 1、检查 必须项 是否存在 2、判断是否需要加密 2.1、如果值没有引用密码库，直接加密 2.2、如果引用了，则不需要加密.
     */
    @Override
    public String checkAndEncrypt(List<PluginConfigFieldEntity> fields, String config, String oldConfig) {
        Map<String, String> map = convertConfigToMap(config);
        Map<String, String> oldMap = convertConfigToMap(oldConfig);
        fields.forEach(pluginConfigField -> {
            Optional<String> valueOptional = Optional.ofNullable(map.get(pluginConfigField.getKey()));
            valueOptional.ifPresentOrElse(value -> {
                if (isFieldNeedEncrypted(pluginConfigField)) {
                    if (isSensitiveFlag(value)) {
                        replaceWithPreConfigOrSensitiveFlag(pluginConfigField.getKey(), oldMap, map);
                    } else if (isNoReferencedPassword(value)) {
                        map.put(pluginConfigField.getKey(), stringEncryptor.encrypt(value));
                    }
                }
            }, () -> throwExceptionWhileFieldIsRequired(pluginConfigField));
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
            setSensitiveIfNecessary(response, pluginConfigField);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    public String decryptToString(List<PluginConfigFieldEntity> fields, String config) {
        Map<String, String> map = convertConfigToMap(config);
        fields.forEach(pluginConfigField -> {
            Optional<String> valueOptional = Optional.ofNullable(map.get(pluginConfigField.getKey()));
            valueOptional.ifPresentOrElse(
                value -> parsingRefOrElseDecrypt(value, map, pluginConfigField),
                () -> throwExceptionWhileFieldIsRequired(pluginConfigField));
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    private Map<String, String> convertConfigToMap(String config) {
        return Try.of(() -> objectMapper.readValue(config, Map.class)).getOrElse(new HashMap<>());
    }

    /**
     * 判断字段是否要求加密.
     */
    private boolean isFieldNeedEncrypted(PluginConfigFieldEntity pluginConfigField) {
        return Objects.equals(pluginConfigField.getEncrypted(), Boolean.TRUE);
    }

    /**
     * 判断值是否是敏感值标志.
     */
    private boolean isSensitiveFlag(String value) {
        return Objects.equals(value, SENSITIVE_FLAG);
    }

    /**
     * 是否引用密钥库.
     */
    private boolean isNoReferencedPassword(String value) {
        return !Pattern.matches(PASSWORD_REGEX, value);
    }

    /**
     * 获取引用的 Password Key.
     */
    private Optional<String> getReferenceKey(String value) {
        Matcher matcher = PASSWORD_PATTERN.matcher(value);
        if (matcher.find()) {
            String group = matcher.group(PASSWORD_KEY_GROUP);
            return Optional.ofNullable(group);
        }
        return Optional.empty();
    }

    private void replaceWithPreConfigOrSensitiveFlag(String key,
        Map<String, String> oldConfigMap, Map<String, String> newConfigMap) {
        Optional.ofNullable(oldConfigMap.get(key))
            .ifPresentOrElse(oldConfig -> newConfigMap.put(key, oldConfig),
                () -> newConfigMap.put(key, stringEncryptor.encrypt(SENSITIVE_FLAG)));
    }

    private void parsingRefOrElseDecrypt(String value, Map<String, String> configMap,
        PluginConfigFieldEntity configField) {
        if (isFieldNeedEncrypted(configField)) {
            getReferenceKey(value).ifPresentOrElse(
                referenceKey -> configMap.put(configField.getKey(), passwordStoreService.getValueByKey(referenceKey)),
                () -> configMap.put(configField.getKey(), stringEncryptor.decrypt(value)));
        }
    }

    /**
     * 如果字段是必须的，则抛出异常.
     */
    private void throwExceptionWhileFieldIsRequired(PluginConfigFieldEntity configField) {
        if (Objects.equals(configField.getRequired(), Boolean.TRUE)) {
            throw new EagleEyeException(PLUGIN_CONFIG_FIELD_MISSING_ERROR, configField.getLabelName());
        }
    }

    /**
     * 如果 - value不为空 - 字段需要加密 - 没有引用密钥库
     * 则 将value设置为敏感值.
     */
    private void setSensitiveIfNecessary(PluginConfigFieldWithValueResponse response, PluginConfigFieldEntity field) {
        if (StringUtils.isNotBlank(response.getValue())
            && isFieldNeedEncrypted(field) && isNoReferencedPassword(response.getValue())) {
            response.setValue(SENSITIVE_FLAG);
        }
    }
}