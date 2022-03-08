package com.sms.eagle.eye.backend.response.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelTypeResponse {

    private Integer id;
    private String name;
    private String icon;
    private Boolean systemChannel;
}