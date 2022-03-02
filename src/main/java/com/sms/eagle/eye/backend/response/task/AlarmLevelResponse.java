package com.sms.eagle.eye.backend.response.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmLevelResponse {

    private String name;
    private Integer value;
    private Boolean isAlarm;
    private String color;
}