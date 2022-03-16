package com.sms.eagle.eye.backend.response.task;

import static com.sms.eagle.eye.backend.common.TimePatternConstant.DATE_TIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    /**
     * 监控任务id.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 监控名称.
     */
    private String name;
    /**
     * 创建人.
     */
    private String creator;
    /**
     * 所属项目.
     */
    private String project;
    /**
     * 插件id.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pluginId;
    /**
     * 任务状态.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer status;
    /**
     * 任务告警级别.
     */
    private Integer alarmLevel;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> tagList;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> groupList;
    /**
     * 创建时间.
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createTime;
}