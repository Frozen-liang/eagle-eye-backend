package com.sms.eagle.eye.backend.resolver.impl;

import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_KEY_GROUP;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_PATTERN;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_REGEX;
import static com.sms.eagle.eye.backend.exception.ErrorCode.MUST_USE_PASSWORD_VAULT;
import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_CONFIG_FIELD_MISSING_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import com.sms.eagle.eye.backend.resolver.ConfigMetadataResolver;
import io.vavr.control.Try;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class ConfigMetadataResolverImpl implements ConfigMetadataResolver {

    private static final String DEFAULT_MAP = "{}";

    private final ObjectMapper objectMapper;
    private final PasswordStoreService passwordStoreService;

    public ConfigMetadataResolverImpl(ObjectMapper objectMapper,
        PasswordStoreService passwordStoreService) {
        this.objectMapper = objectMapper;
        this.passwordStoreService = passwordStoreService;
    }

    @Override
    public Map<String, Object> convertConfigToMap(String config) {
        return Try.of(() -> objectMapper.readValue(config, Map.class)).getOrElse(new HashMap<>());
    }

    /**
     * 检查是否必须 如 加密 则需引用密码库中的密码.
     */
    @Override
    public String checkAndEncrypt(List<ConfigMetadata> metadataList, String config) {
        Map<String, Object> map = convertConfigToMap(config);
        metadataList.forEach(metadata -> {
            Optional<Object> valueOptional = Optional.ofNullable(map.get(metadata.getKey()));
            valueOptional.ifPresentOrElse(value -> {
                if (isFieldNeedEncrypted(metadata) && isNoReferencedPassword(value)) {
                    throw new EagleEyeException(MUST_USE_PASSWORD_VAULT);
                }
            }, () -> throwExceptionWhileFieldIsRequired(metadata));
        });
        return config;
    }

    @Override
    public Object decryptToFrontendValue(ConfigMetadata metadata, Map<String, Object> configMap) {
        return configMap.get(metadata.getKey());
    }

    @Override
    public String decryptToString(List<ConfigMetadata> metadataList, String config) {
        Map<String, Object> map = convertConfigToMap(config);
        metadataList.forEach(metadata -> {
            Optional<Object> valueOptional = Optional.ofNullable(map.get(metadata.getKey()));
            valueOptional.ifPresentOrElse(
                value -> parsingRefOrElseDecrypt(value, map, metadata),
                () -> throwExceptionWhileFieldIsRequired(metadata));
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    /**
     * 判断字段是否要求加密.
     */
    protected boolean isFieldNeedEncrypted(ConfigMetadata configMetadata) {
        return Objects.equals(configMetadata.getEncrypted(), Boolean.TRUE);
    }

    /**
     * 是否引用密钥库.
     */
    protected boolean isNoReferencedPassword(Object value) {
        if (value instanceof String) {
            return !Pattern.matches(PASSWORD_REGEX, (String) value);
        }
        return true;
    }

    /**
     * 获取引用的 Password Key.
     */
    protected Optional<String> getReferenceKey(Object value) {
        if (value instanceof String) {
            Matcher matcher = PASSWORD_PATTERN.matcher((String) value);
            if (matcher.find()) {
                String group = matcher.group(PASSWORD_KEY_GROUP);
                return Optional.ofNullable(group);
            }
        }
        return Optional.empty();
    }

    protected void parsingRefOrElseDecrypt(Object value, Map<String, Object> configMap,
        ConfigMetadata metadata) {
        if (isFieldNeedEncrypted(metadata)) {
            getReferenceKey(value).ifPresentOrElse(referenceKey ->
                configMap.put(metadata.getKey(), passwordStoreService.getValueByKey(referenceKey)), () -> {
                throw new EagleEyeException(MUST_USE_PASSWORD_VAULT);
            });
        }
    }

    /**
     * 如果字段是必须的，则抛出异常.
     */
    protected void throwExceptionWhileFieldIsRequired(ConfigMetadata metadata) {
        if (Objects.equals(metadata.getRequired(), Boolean.TRUE)) {
            throw new EagleEyeException(PLUGIN_CONFIG_FIELD_MISSING_ERROR, metadata.getLabelName());
        }
    }
}