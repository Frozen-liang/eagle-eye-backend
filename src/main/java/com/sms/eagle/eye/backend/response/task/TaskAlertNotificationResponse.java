package com.sms.eagle.eye.backend.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sms.eagle.eye.backend.common.enums.NotificationTemplateType;
import com.sms.eagle.eye.backend.model.TemplateField;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldWithValueResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertNotificationResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long alertNotificationId;
    private Integer channelType;
    private String content;

    @Builder.Default
    private String variableKey = NotificationTemplateType.ALERT.getVariableKey();
    // TODO 告警的fieldList都一样，单独获取即可.
    @Builder.Default
    private List<TemplateField> fieldList = NotificationTemplateType.ALERT.getFieldList();

    private List<ChannelFieldWithValueResponse> input;
}