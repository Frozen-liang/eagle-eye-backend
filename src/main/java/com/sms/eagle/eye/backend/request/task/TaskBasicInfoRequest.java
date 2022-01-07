package com.sms.eagle.eye.backend.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskBasicInfoRequest {

    /**
     * 任务id.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(groups = {UpdateGroup.class})
    private Long id;
    /**
     * 监控任务名称.
     */
    @NotBlank(groups = {InsertGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 所属项目.
     */
    @NotBlank(groups = {InsertGroup.class})
    private String project;
    /**
     * 所属团队.
     */
    private String team;
    /**
     * 监控描述.
     */
    private String description;
    /**
     * 插件id.
     */
    @NotNull(groups = {InsertGroup.class})
    private Long pluginId;
    /**
     * tag列表.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<Long> tagList;
}