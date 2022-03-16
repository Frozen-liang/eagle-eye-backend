package com.sms.eagle.eye.backend.request.task;

import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.model.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskQueryRequest extends PageRequest<TaskEntity> {

    /**
     * 监控任务名称.
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
     * 所属插件id.
     */
    private Long pluginId;
    /**
     * 所属分组id.
     */
    private Long groupId;

}