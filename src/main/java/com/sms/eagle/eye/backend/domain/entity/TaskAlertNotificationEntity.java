package com.sms.eagle.eye.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "task_alert_notification")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskAlertNotificationEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long taskId;
    private Integer alarmLevel;
    private Integer channelType;
    /**
     * 通道实例id，非系统级别的通道类型需要指定通道实例.
     */
    private Long notificationChannelId;
    private String channelInput;
    /**
     * 消息内容.
     */
    private String content;

    private LocalDateTime utcCreateTime;
}