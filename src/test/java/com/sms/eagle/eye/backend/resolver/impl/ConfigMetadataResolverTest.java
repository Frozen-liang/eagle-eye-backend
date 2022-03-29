package com.sms.eagle.eye.backend.resolver.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.MUST_USE_PASSWORD_VAULT;
import static com.sms.eagle.eye.backend.exception.ErrorCode.PLUGIN_CONFIG_FIELD_MISSING_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.ConfigMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ConfigMetadataResolverTest {

    private final ObjectMapper objectMapper = mock(ObjectMapper.class);
    private final PasswordStoreService passwordStoreService = mock(PasswordStoreService.class);

    private final ConfigMetadataResolverImpl resolver = spy(
        new ConfigMetadataResolverImpl(objectMapper, passwordStoreService));

    private static final String EXCEPTION_CODE = "code";

    /**
     * {@link ConfigMetadataResolverImpl#convertConfigToMap(String)}
     *
     * <p>情形1：正常转换为map
     */
    @Test
    void convertConfigToMap_test_1() throws JsonProcessingException {
        String config = "config";
        // mock objectMapper.readValue()
        Map<String, Object> map = mock(Map.class);
        when(objectMapper.readValue(config, Map.class)).thenReturn(map);
        // invoke
        Map<String, Object> result = resolver.convertConfigToMap(config);
        // assert
        assertThat(result).isEqualTo(map);
    }

    /**
     * {@link ConfigMetadataResolverImpl#convertConfigToMap(String)}
     *
     * <p>情形2：转换过程中发生异常，返回空map
     */
    @Test
    void convertConfigToMap_test_2() throws JsonProcessingException {
        String config = "config";
        // mock objectMapper.readValue
        when(objectMapper.readValue(config, Map.class)).thenThrow(new RuntimeException());
        // invoke
        Map<String, Object> result = resolver.convertConfigToMap(config);
        // assert
        assertThat(result).isEmpty();
    }

    /**
     * {@link ConfigMetadataResolverImpl#checkAndEncrypt(List, String)}
     *
     * <p>情形1：字段必须，但config当中不存在
     */
    @Test
    void checkAndEncrypt_test_1() {
        // prepare request
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        List<ConfigMetadata> metadataList = List.of(configMetadata);
        String config = "config";
        // mock convertConfigToMap
        Map<String, Object> map = mock(Map.class);
        when(resolver.convertConfigToMap(config)).thenReturn(map);
        // mock configMetadata.getKey()
        String key = "key";
        when(configMetadata.getKey()).thenReturn(key);
        // mock map.get
        when(map.get(key)).thenReturn(null);
        // mock throwExceptionWhileFieldIsRequired
        doThrow(new RuntimeException()).when(resolver).throwExceptionWhileFieldIsRequired(configMetadata);
        // invoke and assert
        assertThatThrownBy(() -> resolver.checkAndEncrypt(metadataList, config)).isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link ConfigMetadataResolverImpl#checkAndEncrypt(List, String)}
     *
     * <p>情形2：元数据中表示字段需要加密，但config中字段的值没有引用密钥库里的密码
     */
    @Test
    void checkAndEncrypt_test_2() {
        // prepare request
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        List<ConfigMetadata> metadataList = List.of(configMetadata);
        String config = "config";
        // mock convertConfigToMap
        Map<String, Object> map = mock(Map.class);
        when(resolver.convertConfigToMap(config)).thenReturn(map);
        // mock configMetadata.getKey()
        String key = "key";
        when(configMetadata.getKey()).thenReturn(key);
        // mock map.get
        Object value = 123;
        when(map.get(key)).thenReturn(value);
        // mock isFieldNeedEncrypted()
        when(resolver.isFieldNeedEncrypted(configMetadata)).thenReturn(Boolean.TRUE);
        // mock isNoReferencedPassword()
        when(resolver.isNoReferencedPassword(value)).thenReturn(Boolean.TRUE);
        // invoke and assert
        assertThatThrownBy(() -> resolver.checkAndEncrypt(metadataList, config))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(MUST_USE_PASSWORD_VAULT.getCode());
    }

    /**
     * {@link ConfigMetadataResolverImpl#checkAndEncrypt(List, String)}
     *
     * <p>情形3：经过校验，返回原来的config
     */
    @Test
    void checkAndEncrypt_test_3() {
        // prepare request
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        List<ConfigMetadata> metadataList = List.of(configMetadata);
        String config = "config";
        // mock convertConfigToMap
        Map<String, Object> map = mock(Map.class);
        when(resolver.convertConfigToMap(config)).thenReturn(map);
        // mock configMetadata.getKey()
        String key = "key";
        when(configMetadata.getKey()).thenReturn(key);
        // mock map.get
        Object value = 123;
        when(map.get(key)).thenReturn(value);
        // mock isFieldNeedEncrypted()
        when(resolver.isFieldNeedEncrypted(configMetadata)).thenReturn(Boolean.FALSE);
        // invoke
        String result = resolver.checkAndEncrypt(metadataList, config);
        // assert
        assertThat(result).isEqualTo(config);
    }

    /**
     * {@link ConfigMetadataResolverImpl#decryptToFrontendValue(ConfigMetadata, Map)}
     */
    @Test
    void decryptToFrontendValue_test_1() {
        // prepare request
        Map<String, Object> configMap = mock(Map.class);
        ConfigMetadata metadata = mock(ConfigMetadata.class);
        // mock metadata.getKey()
        String key = "key";
        when(metadata.getKey()).thenReturn(key);
        // mock map.get
        Object value = 123;
        when(configMap.get(key)).thenReturn(value);
        // invoke
        Object result = resolver.decryptToFrontendValue(metadata, configMap);
        // assert
        assertThat(result).isEqualTo(value);
    }

    /**
     * {@link ConfigMetadataResolverImpl#decryptToString(List, String)}
     *
     * <p>情形1：字段必须，但config当中不存在
     */
    @Test
    void decryptToString_test_1() {
        // prepare request
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        List<ConfigMetadata> metadataList = List.of(configMetadata);
        String config = "config";
        // mock convertConfigToMap
        Map<String, Object> map = mock(Map.class);
        when(resolver.convertConfigToMap(config)).thenReturn(map);
        // mock configMetadata.getKey()
        String key = "key";
        when(configMetadata.getKey()).thenReturn(key);
        // mock map.get
        when(map.get(key)).thenReturn(null);
        // mock throwExceptionWhileFieldIsRequired
        doThrow(new RuntimeException()).when(resolver).throwExceptionWhileFieldIsRequired(configMetadata);
        // invoke and assert
        assertThatThrownBy(() -> resolver.decryptToString(metadataList, config)).isInstanceOf(RuntimeException.class);
    }

    /**
     * {@link ConfigMetadataResolverImpl#decryptToString(List, String)}
     *
     * <p>情形2：对引用了密码库密码的字段值进行替换
     */
    @Test
    void decryptToString_test_2() {
        // prepare request
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        List<ConfigMetadata> metadataList = List.of(configMetadata);
        String config = "config";
        // mock convertConfigToMap
        Map<String, Object> map = mock(Map.class);
        when(resolver.convertConfigToMap(config)).thenReturn(map);
        // mock configMetadata.getKey()
        String key = "key";
        when(configMetadata.getKey()).thenReturn(key);
        // mock map.get
        Object value = 123412;
        when(map.get(key)).thenReturn(value);
        // mock parsingRefOrElseDecrypt
        doNothing().when(resolver).parsingRefOrElseDecrypt(value, map, configMetadata);
        // invoke
        resolver.decryptToString(metadataList, config);
        // verify
        verify(resolver).parsingRefOrElseDecrypt(value, map, configMetadata);
    }

    /**
     * {@link ConfigMetadataResolverImpl#isFieldNeedEncrypted(ConfigMetadata)}
     */
    @Test
    void isFieldNeedEncrypted_test_1() {
        ConfigMetadata configMetadata = mock(ConfigMetadata.class);
        // mock configMetadata.getEncrypted()
        when(configMetadata.getEncrypted()).thenReturn(Boolean.FALSE);
        // invoke
        boolean result = resolver.isFieldNeedEncrypted(configMetadata);
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link ConfigMetadataResolverImpl#isNoReferencedPassword(Object)}
     *
     * <p>情形1：value 是 string 类型，但与引用密钥库的标志不匹配
     */
    @Test
    void isNoReferencedPassword_test_1() {
        Object value = "123";
        // invoke
        boolean result = resolver.isNoReferencedPassword(value);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link ConfigMetadataResolverImpl#isNoReferencedPassword(Object)}
     *
     * <p>情形2：value 是 string 类型，并且与引用密钥库的标志相匹配
     */
    @Test
    void isNoReferencedPassword_test_2() {
        Object value = "EAGLE#username#VAULT";
        // invoke
        boolean result = resolver.isNoReferencedPassword(value);
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link ConfigMetadataResolverImpl#isNoReferencedPassword(Object)}
     *
     * <p>情形3：value 不是 string 类型
     */
    @Test
    void isNoReferencedPassword_test_3() {
        Object value = 123;
        // invoke
        boolean result = resolver.isNoReferencedPassword(value);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link ConfigMetadataResolverImpl#getReferenceKey(Object)}
     *
     * <p>情形1：value 是 string 类型，根据正则匹配得到了密钥库的key
     */
    @Test
    void getReferenceKey_test_1() {
        Object value = "EAGLE#username#VAULT";
        // invoke
        Optional<String> result = resolver.getReferenceKey(value);
        // assert
        assertThat(result.isPresent()).isTrue();
    }

    /**
     * {@link ConfigMetadataResolverImpl#getReferenceKey(Object)}
     *
     * <p>情形2：value 是 string 类型，根据正则匹配没找到密钥库的key
     */
    @Test
    void getReferenceKey_test_2() {
        Object value = "test";
        // invoke
        Optional<String> result = resolver.getReferenceKey(value);
        // assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link ConfigMetadataResolverImpl#getReferenceKey(Object)}
     *
     * <p>情形3：value 不是 string 类型
     */
    @Test
    void getReferenceKey_test_3() {
        Object value = 123;
        // invoke
        Optional<String> result = resolver.getReferenceKey(value);
        // assert
        assertThat(result.isPresent()).isFalse();
    }

    /**
     * {@link ConfigMetadataResolverImpl#parsingRefOrElseDecrypt(Object, Map, ConfigMetadata)}
     *
     * <p>情形1：字段需要加密，根据value值得到了引用的密钥key，
     * 再从密码库中查询出真实密码放入configMap里
     */
    @Test
    void parsingRefOrElseDecrypt_test_1() {
        Object value = "123";
        Map<String, Object> configMap = new HashMap<>();
        ConfigMetadata metadata = mock(ConfigMetadata.class);
        // mock metadata.getKey()
        String metadataKey = "metadataKey";
        when(metadata.getKey()).thenReturn(metadataKey);
        // mock isFieldNeedEncrypted()
        when(resolver.isFieldNeedEncrypted(metadata)).thenReturn(Boolean.TRUE);
        // mock getReferenceKey()
        String secretKey = "secretKey";
        when(resolver.getReferenceKey(value)).thenReturn(Optional.of(secretKey));
        // mock passwordStoreService.getValueByKey()
        String secret = "secret";
        when(passwordStoreService.getValueByKey(secretKey)).thenReturn(secret);
        // invoke
        resolver.parsingRefOrElseDecrypt(value, configMap, metadata);
        // assert
        assertThat(configMap.get(metadataKey)).isEqualTo(secret);
    }

    /**
     * {@link ConfigMetadataResolverImpl#parsingRefOrElseDecrypt(Object, Map, ConfigMetadata)}
     *
     * <p>情形2：字段需要加密，根据value没有找到引用的密钥key
     */
    @Test
    void parsingRefOrElseDecrypt_test_2() {
        Object value = "123";
        Map<String, Object> configMap = new HashMap<>();
        ConfigMetadata metadata = mock(ConfigMetadata.class);
        // mock isFieldNeedEncrypted()
        when(resolver.isFieldNeedEncrypted(metadata)).thenReturn(Boolean.TRUE);
        // mock getReferenceKey()
        when(resolver.getReferenceKey(value)).thenReturn(Optional.empty());
        // invoke and assert
       assertThatThrownBy(() ->  resolver.parsingRefOrElseDecrypt(value, configMap, metadata))
           .isInstanceOf(EagleEyeException.class)
           .extracting(EXCEPTION_CODE).isEqualTo(MUST_USE_PASSWORD_VAULT.getCode());
    }

    /**
     * {@link ConfigMetadataResolverImpl#throwExceptionWhileFieldIsRequired(ConfigMetadata)}
     */
    @Test
    void throwExceptionWhileFieldIsRequired_test_1() {
        ConfigMetadata metadata = mock(ConfigMetadata.class);
        when(metadata.getRequired()).thenReturn(Boolean.TRUE);
        // invoke
        assertThatThrownBy(() ->  resolver.throwExceptionWhileFieldIsRequired(metadata))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(PLUGIN_CONFIG_FIELD_MISSING_ERROR.getCode());
    }
}