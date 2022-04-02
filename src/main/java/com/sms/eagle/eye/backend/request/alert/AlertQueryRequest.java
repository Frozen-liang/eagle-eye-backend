package com.sms.eagle.eye.backend.request.alert;

import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.request.PageRequest;
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
public class AlertQueryRequest extends PageRequest<AlertEntity> {

    /**
     * 预警任务名称.
     */
    private String taskName;
    /**
     * 所属项目.
     */
    private String project;
    private String from;
    private String to;
}