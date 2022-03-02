package com.sms.eagle.eye.backend.resolver.impl;

import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_KEY_GROUP;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_PATTERN;
import static com.sms.eagle.eye.backend.domain.service.impl.PasswordStoreServiceImpl.PASSWORD_REGEX;
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
import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

@Component
public class ConfigMetadataResolverImpl implements ConfigMetadataResolver {

    private static final String DEFAULT_MAP = "{}";
    private static final String SENSITIVE_FLAG = "[EAGLE-ENCRYPTED-VALUE]";

    private final ObjectMapper objectMapper;
    private final StringEncryptor stringEncryptor;
    private final PasswordStoreService passwordStoreService;

    public ConfigMetadataResolverImpl(ObjectMapper objectMapper, StringEncryptor stringEncryptor,
        PasswordStoreService passwordStoreService) {
        this.objectMapper = objectMapper;
        this.stringEncryptor = stringEncryptor;
        this.passwordStoreService = passwordStoreService;
    }

    @Override
    public Map<String, String> convertConfigToMap(String config) {
        return Try.of(() -> objectMapper.readValue(config, Map.class)).getOrElse(new HashMap<>());
    }

    @Override
    public String checkAndEncrypt(List<ConfigMetadata> metadataList, String config, String oldConfig) {
        Map<String, String> map = convertConfigToMap(config);
        Map<String, String> oldMap = convertConfigToMap(oldConfig);
        metadataList.forEach(metadata -> {
            Optional<String> valueOptional = Optional.ofNullable(map.get(metadata.getKey()));
            valueOptional.ifPresentOrElse(value -> {
                if (isFieldNeedEncrypted(metadata)) {
                    if (isSensitiveFlag(value)) {
                        replaceWithPreConfigOrSensitiveFlag(metadata.getKey(), oldMap, map);
                    } else if (isNoReferencedPassword(value)) {
                        map.put(metadata.getKey(), stringEncryptor.encrypt(value));
                    }
                }
            }, () -> throwExceptionWhileFieldIsRequired(metadata));
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    @Override
    public String decryptToFrontendValue(ConfigMetadata metadata, Map<String, String> configMap) {
        String value = configMap.get(metadata.getKey());
        return setSensitiveIfNecessary(value, metadata);
    }

    @Override
    public String decryptToString(List<ConfigMetadata> metadataList, String config) {
        Map<String, String> map = convertConfigToMap(config);
        metadataList.forEach(metadata -> {
            Optional<String> valueOptional = Optional.ofNullable(map.get(metadata.getKey()));
            valueOptional.ifPresentOrElse(
                value -> parsingRefOrElseDecrypt(value, map, metadata),
                () -> throwExceptionWhileFieldIsRequired(metadata));
        });
        return Try.of(() -> objectMapper.writeValueAsString(map)).getOrElse(DEFAULT_MAP);
    }

    /**
     * 判断字段是否要求加密.
     */
    private boolean isFieldNeedEncrypted(ConfigMetadata configMetadata) {
        return Objects.equals(configMetadata.getEncrypted(), Boolean.TRUE);
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
        ConfigMetadata metadata) {
        if (isFieldNeedEncrypted(metadata)) {
            getReferenceKey(value).ifPresentOrElse(
                referenceKey -> configMap.put(metadata.getKey(), passwordStoreService.getValueByKey(referenceKey)),
                () -> configMap.put(metadata.getKey(), stringEncryptor.decrypt(value)));
        }
    }

    /**
     * 如果字段是必须的，则抛出异常.
     */
    private void throwExceptionWhileFieldIsRequired(ConfigMetadata metadata) {
        if (Objects.equals(metadata.getRequired(), Boolean.TRUE)) {
            throw new EagleEyeException(PLUGIN_CONFIG_FIELD_MISSING_ERROR, metadata.getLabelName());
        }
    }

    /**
     * 如果 - value不为空 - 字段需要加密 - 没有引用密钥库
     * 则 将value设置为敏感值.
     */
    private String setSensitiveIfNecessary(String value, ConfigMetadata metadata) {
        if (StringUtils.isNotBlank(value)
            && isFieldNeedEncrypted(metadata) && isNoReferencedPassword(value)) {
            return SENSITIVE_FLAG;
        }
        return value;
    }
}