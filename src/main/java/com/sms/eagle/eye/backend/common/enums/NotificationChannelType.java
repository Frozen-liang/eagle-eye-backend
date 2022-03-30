package com.sms.eagle.eye.backend.common.enums;

import static com.sms.eagle.eye.backend.exception.ErrorCode.CHANNEL_TYPE_IS_NOT_CORRECT;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.response.channel.ChannelTypeResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum NotificationChannelType {

    EMAIL(1, "Email", Boolean.TRUE),
    WECOM(2, "WeCom", Boolean.FALSE);

    private static final Map<Integer, NotificationChannelType> MAP;
    private static final List<ChannelTypeResponse> LIST;

    static {
        LIST = Arrays.stream(values()).map(type -> ChannelTypeResponse.builder()
                .id(type.getValue())
                .name(type.getName())
                .systemChannel(type.getSystemChannel())
                .build())
            .collect(Collectors.toList());
        MAP = Arrays.stream(values()).collect(
            Collectors.toMap(NotificationChannelType::getValue, Function.identity()));
    }

    private final Integer value;
    private final String name;
    private final Boolean systemChannel;

    NotificationChannelType(Integer value, String name, Boolean systemChannel) {
        this.value = value;
        this.name = name;
        this.systemChannel = systemChannel;
    }

    public static NotificationChannelType resolve(Integer value) {
        return Optional.ofNullable(MAP.get(value))
            .orElseThrow(() -> new EagleEyeException(CHANNEL_TYPE_IS_NOT_CORRECT));
    }

    public static List<ChannelTypeResponse> getList() {
        return LIST;
    }
}
