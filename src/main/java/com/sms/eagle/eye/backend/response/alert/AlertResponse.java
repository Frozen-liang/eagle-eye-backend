package com.sms.eagle.eye.backend.response.alert;

import static com.sms.eagle.eye.backend.common.TimePatternConstant.DATE_TIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String taskName;
    private String project;
    private String team;
    private String description;
    private String pluginName;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime alertTime;
}