package com.sms.eagle.eye.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "task_group_mapping")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroupMappingEntity {

    private Long id;
    private Long taskId;
    private Long groupId;
    private LocalDateTime utcCreateTime;

}