package com.sms.eagle.eye.backend.aws.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwsAlertRuleDto {

    private Integer interval;
    private String rule;
}