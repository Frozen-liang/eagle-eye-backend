package com.sms.eagle.eye.backend.listener.resolver;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum AwsMessageGroup {

    INVOKE_ERROR("Error"),
    ALARM("Alarm");

    private static final Map<String, AwsMessageGroup> MAP;

    static {
        MAP = Arrays.stream(values()).collect(Collectors.toMap(AwsMessageGroup::getName, Function.identity()));
    }

    private final String name;

    AwsMessageGroup(String name) {
        this.name = name;
    }

    public static Optional<AwsMessageGroup> resolve(String name) {
        return Optional.ofNullable(MAP.get(name));
    }
}
