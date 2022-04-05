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
    /**
     * 预警任务名称.
     */
    private String taskName;
    /**
     * 所属项目.
     */
    private String project;
    /**
     * 告警信息.
     */
    private String description;

    private String pluginName;
    /**
     * 任务告警级别.
     */
    private Integer alarmLevel;
    /**
     * 告警信息.
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime alertTime;
}