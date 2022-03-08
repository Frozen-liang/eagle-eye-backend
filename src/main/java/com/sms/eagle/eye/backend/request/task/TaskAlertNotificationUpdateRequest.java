package com.sms.eagle.eye.backend.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sms.eagle.eye.backend.utils.KeepAsJsonDeserializer;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertNotificationUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long alertNotificationId;
    /**
     * 任务告警规则.
     */
    @NotNull
    @JsonDeserialize(using = KeepAsJsonDeserializer.class)
    private String channelInput;
    /**
     * 消息内容.
     */
    @NotNull
    private String content;

}