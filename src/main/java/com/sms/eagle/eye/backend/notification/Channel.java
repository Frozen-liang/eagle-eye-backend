package com.sms.eagle.eye.backend.notification;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_CHECK_ERROR;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_METADATA_ERROR;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.NotificationEvent;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import io.vavr.control.Try;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

public interface Channel {

    TypeReference<List<ChannelFieldResponse>> CHANNEL_FIELD_TYPE_REF = new TypeReference<>() {
    };

    List<ChannelFieldResponse> getConfigFields(Integer type);

    List<ChannelFieldResponse> getTaskInputFields(Integer type);

    boolean notify(NotificationEvent event);

    default List<ChannelFieldResponse> readMetadataFromResource(Resource resource, ObjectMapper objectMapper) {
        try {
            String metadata = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            return objectMapper.readValue(metadata, CHANNEL_FIELD_TYPE_REF);
        } catch (Exception exception) {
            throw new EagleEyeException(GET_METADATA_ERROR);
        }
    }

    default Map<String, Object> convertToMap(ObjectMapper objectMapper, String value) {
        return Try.of(() -> objectMapper.readValue(value, Map.class)).getOrElse(new HashMap<>());
    }

    /**
     * 从map中取出指定key的value.
     * <li>value不存在：如果字段是必须的，则抛出异常；否则返回默认值.
     * <li>value存在：判断value类型，如果与提供的 clazz 一致，则将value返回；否则抛出异常.
     *
     * @param map 字段键值对的集合
     * @param key 指定的key
     * @param clazz 取出的值类型
     * @param required 字段是否必须
     * @param defaultValue 字段默认值
     */
    default <T> T getValueFromMap(Map<String, Object> map, String key, Class<T> clazz,
        boolean required, T defaultValue) {
        Object value = map.get(key);
        if (isNull(value)) {
            if (required) {
                throw new EagleEyeException(CHANNEL_CHECK_ERROR, key);
            }
            return defaultValue;
        } else {
            if (clazz.isAssignableFrom(value.getClass())) {
                return (T) value;
            }
            throw new EagleEyeException(CHANNEL_CHECK_ERROR, key);
        }
    }

    /**
     * 先判断是否为空，不为空再根据value类型进行进一步判断.
     * <li>是string类型，则判断是不是空字符串
     * <li>是list类型，则判断是不是空列表
     */
    default boolean isNull(Object value) {
        if (Objects.isNull(value)) {
            return true;
        }
        Class<?> clazz = value.getClass();
        if (String.class.isAssignableFrom(clazz)) {
            return StringUtils.isBlank((String) value);
        } else if (List.class.isAssignableFrom(clazz)) {
            return CollectionUtils.isEmpty((List) value);
        } else {
            return false;
        }
    }

    default Context getTemplateContext(String variableKey, Object variable) {
        Context context = new Context();
        context.setVariable(variableKey, variable);
        return context;
    }
}