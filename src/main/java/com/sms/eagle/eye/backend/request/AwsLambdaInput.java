package com.sms.eagle.eye.backend.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwsLambdaInput {

    private String id;
    private String name;
    private String description;
    private String decryptKey;
    private String payload;
    private Integer interval;
    private String pluginUrl;
    private String queueUrl;
    // TODO 可以改成单个，lambda也需要改.
    private List<String> alertRule;
}