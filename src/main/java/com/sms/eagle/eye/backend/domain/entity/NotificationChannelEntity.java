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

@TableName(value = "notification_channel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationChannelEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private String name;
    private Integer type;
    private String config;
    private String creator;
    private LocalDateTime utcCreateTime;
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}