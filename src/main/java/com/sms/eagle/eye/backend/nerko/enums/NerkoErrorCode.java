package com.sms.eagle.eye.backend.nerko.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum NerkoErrorCode {

    UN_AUTHORIZED("-1", "401"),
    SUCCESS("200", ""),
    UNKNOWN("999999", "UNKNOWN");

    private final String code;
    private final String description;

    private static final Map<String, NerkoErrorCode> MAP;

    static {
        MAP = Arrays.stream(values()).collect(Collectors.toMap(NerkoErrorCode::getCode, Function.identity()));
    }

    NerkoErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static NerkoErrorCode resolve(String code) {
        return Optional.ofNullable(MAP.get(code)).orElse(UNKNOWN);
    }
}
