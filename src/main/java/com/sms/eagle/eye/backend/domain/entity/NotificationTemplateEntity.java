package com.sms.eagle.eye.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "notification_template")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTemplateEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Integer channelType;
    private Integer templateType;
    private String template;

    private LocalDateTime utcCreateTime;
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}