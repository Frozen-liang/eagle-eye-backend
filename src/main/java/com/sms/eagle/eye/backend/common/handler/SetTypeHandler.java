package com.sms.eagle.eye.backend.common.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import org.springframework.stereotype.Component;

public class SetTypeHandler extends AbstractJsonTypeHandler<Set<String>> {

    private final ObjectMapper objectMapper;
    private static final String PARSE_ERROR_MESSAGE = "%s parse to set error!";
    private static final String TO_JSON_ERROR_MESSAGE = "%s convert to json error!";

    public SetTypeHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Set<String> parse(String json) {
        return Try.of(() -> objectMapper.readValue(json, new TypeReference<Set<String>>() {
        })).getOrElseThrow(() -> new IllegalArgumentException(String.format(PARSE_ERROR_MESSAGE, json)));
    }

    @Override
    public String toJson(Set<String> obj) {
        return Try.of(() -> objectMapper.writeValueAsString(obj))
            .getOrElseThrow(() -> new IllegalArgumentException(String.format(TO_JSON_ERROR_MESSAGE, obj)));
    }
}
