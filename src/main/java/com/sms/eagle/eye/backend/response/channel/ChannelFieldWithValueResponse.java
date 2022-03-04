package com.sms.eagle.eye.backend.response.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelFieldWithValueResponse extends ChannelFieldResponse {

    public static final Object DEFAULT_VALUE = "";

    @Builder.Default
    private Object value = DEFAULT_VALUE;
}