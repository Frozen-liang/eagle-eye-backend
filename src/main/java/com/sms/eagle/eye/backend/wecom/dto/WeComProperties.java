package com.sms.eagle.eye.backend.wecom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeComProperties {

    private String corpId;
    private String corpSecret;

}