package com.sms.eagle.eye.backend.request.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelMappingRequest {

    private String systemLevel;
    private String mappingLevel;
}