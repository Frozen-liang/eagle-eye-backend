package com.sms.eagle.eye.backend.request.alert;

import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
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
public class AlertQueryRequest extends PageRequest<AlertEntity> {

    private String taskName;
    private String project;
    private String from;
    private String to;
}