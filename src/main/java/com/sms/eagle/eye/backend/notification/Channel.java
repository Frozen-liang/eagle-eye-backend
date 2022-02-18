package com.sms.eagle.eye.backend.notification;

import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_METADATA_ERROR;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

public interface Channel {

    TypeReference<List<ChannelFieldResponse>> CHANNEL_FIELD_TYPE_REF = new TypeReference<>() {};

    List<ChannelFieldResponse> getConfigFields(Integer type);

    List<ChannelFieldResponse> getTaskInputFields(Integer type);

    boolean send(Integer type, String config, String input);

    default List<ChannelFieldResponse> readMetadataFromResource(Resource resource, ObjectMapper objectMapper) {
        try {
            String metadata = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            return objectMapper.readValue(metadata, CHANNEL_FIELD_TYPE_REF);
        } catch (Exception exception) {
            throw new EagleEyeException(GET_METADATA_ERROR);
        }
    }
}