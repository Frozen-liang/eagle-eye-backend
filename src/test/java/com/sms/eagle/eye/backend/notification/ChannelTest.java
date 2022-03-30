package com.sms.eagle.eye.backend.notification;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_CHECK_ERROR;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_METADATA_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

public class ChannelTest {

    private final Channel channel = spy(Channel.class);

    private static final String EXCEPTION_CODE = "code";

    private static final MockedStatic<IOUtils> IO_UTILS_MOCKED_STATIC
        = mockStatic(IOUtils.class);

    @AfterAll
    public static void close() {
        IO_UTILS_MOCKED_STATIC.close();
    }

    /**
     * {@link Channel#readMetadataFromResource(Resource, ObjectMapper)}
     *
     * <p>情形1：读取Resource失败
     */
    @Test
    void readMetadataFromResource_test_1() throws IOException {
        // mock Resource
        Resource resource = mock(Resource.class);
        InputStream inputStream = mock(InputStream.class);
        when(resource.getInputStream()).thenReturn(inputStream);
        // mock objectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // mock IOUtils.toString
        IO_UTILS_MOCKED_STATIC.when(() -> IOUtils.toString(inputStream, StandardCharsets.UTF_8))
            .thenThrow(new RuntimeException());
        // invoke and assert
        assertThatThrownBy(() -> channel.readMetadataFromResource(resource, objectMapper))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(GET_METADATA_ERROR.getCode());
    }

    /**
     * {@link Channel#readMetadataFromResource(Resource, ObjectMapper)}
     *
     * <p>情形2：objectMapper 转换失败
     */
    @Test
    void readMetadataFromResource_test_2() throws IOException {
        // mock Resource
        Resource resource = mock(Resource.class);
        InputStream inputStream = mock(InputStream.class);
        when(resource.getInputStream()).thenReturn(inputStream);
        // mock objectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // mock IOUtils.toString
        String metadata = "metadata";
        IO_UTILS_MOCKED_STATIC.when(() -> IOUtils.toString(inputStream, StandardCharsets.UTF_8))
            .thenReturn(metadata);
        // mock objectMapper.readValue()
        when(objectMapper.readValue(anyString(), any(TypeReference.class))).thenThrow(new RuntimeException());
        // invoke and assert
        assertThatThrownBy(() -> channel.readMetadataFromResource(resource, objectMapper))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(GET_METADATA_ERROR.getCode());
    }

    /**
     * {@link Channel#readMetadataFromResource(Resource, ObjectMapper)}
     *
     * <p>情形3：正常读取并转换
     */
    @Test
    void readMetadataFromResource_test_3() throws IOException {
        // mock Resource
        Resource resource = mock(Resource.class);
        InputStream inputStream = mock(InputStream.class);
        when(resource.getInputStream()).thenReturn(inputStream);
        // mock objectMapper
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        // mock IOUtils.toString
        String metadata = "metadata";
        IO_UTILS_MOCKED_STATIC.when(() -> IOUtils.toString(inputStream, StandardCharsets.UTF_8))
            .thenReturn(metadata);
        // mock objectMapper.readValue()
        ChannelFieldResponse channelFieldResponse = mock(ChannelFieldResponse.class);
        List<ChannelFieldResponse> list = List.of(channelFieldResponse);
        doReturn(list).when(objectMapper).readValue(anyString(), any(TypeReference.class));
        // invoke
        List<ChannelFieldResponse> result = channel.readMetadataFromResource(resource, objectMapper);
        // assert
        assertThat(result).isEqualTo(list);
    }

    /**
     * {@link Channel#convertToMap(ObjectMapper, String)}
     *
     * <p>情形1：正常转换为map
     */
    @Test
    void convertToMap_test_1() throws JsonProcessingException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        String value = "value";
        // mock objectMapper.readValue()
        Map<String, Object> map = mock(Map.class);
        when(objectMapper.readValue(value, Map.class)).thenReturn(map);
        // invoke
        Map<String, Object> result = channel.convertToMap(objectMapper, value);
        // assert
        assertThat(result).isEqualTo(map);
    }

    /**
     * {@link Channel#convertToMap(ObjectMapper, String)}
     *
     * <p>情形2：转换过程中发生异常，返回空map
     */
    @Test
    void convertToMap_test_2() throws JsonProcessingException {
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        String value = "value";
        // mock objectMapper.readValue
        when(objectMapper.readValue(value, Map.class)).thenThrow(new RuntimeException());
        // invoke
        Map<String, Object> result = channel.convertToMap(objectMapper, value);
        // assert
        assertThat(result).isEmpty();
    }

    /**
     * {@link Channel#getValueFromMap(Map, String, Class, boolean, Object)}
     *
     * <p>情形1：map中key对应的value为空，但要求是必须的
     */
    @Test
    void getValueFromMap_test_1() {
        Map<String, Object> map = mock(Map.class);
        String key = "key";
        Class clazz = String.class;
        boolean required = true;
        // mock map.get
        Object value = mock(Object.class);
        when(map.get(key)).thenReturn(value);
        // mock isNull()
        when(channel.isNull(value)).thenReturn(true);
        // invoke and assert
        assertThatThrownBy(() -> channel.getValueFromMap(map, key, clazz, required, null))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(CHANNEL_CHECK_ERROR.getCode());
    }

    /**
     * {@link Channel#getValueFromMap(Map, String, Class, boolean, Object)}
     *
     * <p>情形2：map中key对应的value为空，但要求不是必须的
     */
    @Test
    void getValueFromMap_test_2() {
        Map<String, Object> map = mock(Map.class);
        String key = "key";
        Class clazz = String.class;
        boolean required = false;
        String defaultValue = "123";
        // mock map.get
        Object value = mock(Object.class);
        when(map.get(key)).thenReturn(value);
        // mock isNull()
        when(channel.isNull(value)).thenReturn(true);
        // invoke
        String result = channel.getValueFromMap(map, key, clazz, required, defaultValue);
        // assert
        assertThat(result).isEqualTo(defaultValue);
    }

    /**
     * {@link Channel#getValueFromMap(Map, String, Class, boolean, Object)}
     *
     * <p>情形3：map中key对应的value不为空，且与要求的类型一致
     */
    @Test
    void getValueFromMap_test_3() {
        Map<String, Object> map = mock(Map.class);
        String key = "key";
        boolean required = true;
        // mock map.get
        Object value = "123";
        when(map.get(key)).thenReturn(value);
        // mock isNull()
        when(channel.isNull(value)).thenReturn(false);
        // invoke
        String result = channel.getValueFromMap(map, key, String.class, required, null);
        // assert
        assertThat(result).isEqualTo(value);
    }

    /**
     * {@link Channel#getValueFromMap(Map, String, Class, boolean, Object)}
     *
     * <p>情形4：map中key对应的value不为空，但与要求的类型不一致
     */
    @Test
    void getValueFromMap_test_4() {
        Map<String, Object> map = mock(Map.class);
        String key = "key";
        boolean required = true;
        // mock map.get
        Object value = 12;
        when(map.get(key)).thenReturn(value);
        // mock isNull()
        when(channel.isNull(value)).thenReturn(false);
        // invoke and assert
        assertThatThrownBy(() -> channel.getValueFromMap(map, key, String.class, required, null))
            .isInstanceOf(EagleEyeException.class)
            .extracting(EXCEPTION_CODE).isEqualTo(CHANNEL_CHECK_ERROR.getCode());
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形1：传入null
     */
    @Test
    void isNull_test_1() {
        // invoke
        boolean result = channel.isNull(null);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形2：传入空字符串
     */
    @Test
    void isNull_test_2() {
        // invoke
        boolean result = channel.isNull("");
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形3：传入非空字符串
     */
    @Test
    void isNull_test_3() {
        // invoke
        boolean result = channel.isNull("123");
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形4：传入空列表
     */
    @Test
    void isNull_test_4() {
        // invoke
        boolean result = channel.isNull(Collections.emptyList());
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形5：传入空列表
     */
    @Test
    void isNull_test_5() {
        // invoke
        boolean result = channel.isNull(List.of("123"));
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link Channel#isNull(Object)}
     *
     * <p>情形6：传入一个数字
     */
    @Test
    void isNull_test_6() {
        // invoke
        boolean result = channel.isNull(1);
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link Channel#getTemplateContext(String, Object)}
     */
    @Test
    void getTemplateContext_test_1() {
        String variableKey = "variableKey";
        Object variable = 123;
        // invoke
        Context context = channel.getTemplateContext(variableKey, variable);
        // assert
        assertThat(context).isNotNull();
        assertThat(context.getVariable(variableKey)).isEqualTo(variable);
    }
}