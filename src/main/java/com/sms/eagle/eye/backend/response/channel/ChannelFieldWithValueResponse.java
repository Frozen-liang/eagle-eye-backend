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
public class ChannelFieldWithValueResponse extends ChannelFieldResponse{

    public static final String DEFAULT_VALUE = "";

    @Builder.Default
    private String value = DEFAULT_VALUE;
}