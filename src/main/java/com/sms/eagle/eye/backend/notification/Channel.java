package com.sms.eagle.eye.backend.notification;

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
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;

public interface Channel {

    TypeReference<List<ChannelFieldResponse>> CHANNEL_FIELD_TYPE_REF = new TypeReference<>() {};

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

    default Map<String, String> convertToMap(ObjectMapper objectMapper, String value) {
        return Try.of(() -> objectMapper.readValue(value, Map.class)).getOrElse(new HashMap<>());
    }

    default Context getTemplateContext(String variableKey, Object variable) {
        Context context = new Context();
        context.setVariable(variableKey, variable);
        return context;
    }
}