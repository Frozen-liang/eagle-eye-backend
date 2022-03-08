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
public class TaskAlertNotificationAddRequest {


    /**
     * 任务id.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskId;
    /**
     * 告警级别.
     */
    @NotNull
    private Integer alarmLevel;
    /**
     * 告警通道类型.
     */
    @NotNull
    private Integer channelType;

    /**
     * 通道实例id.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long notificationChannelId;
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