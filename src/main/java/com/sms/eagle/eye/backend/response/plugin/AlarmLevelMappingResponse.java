package com.sms.eagle.eye.backend.response.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelMappingResponse {

    private String systemLevel;
    private String mappingLevel;
}