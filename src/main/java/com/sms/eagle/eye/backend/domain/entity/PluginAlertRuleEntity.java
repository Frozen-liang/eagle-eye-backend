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

@TableName(value = "plugin_alert_rule")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginAlertRuleEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long pluginId;
    private Integer alarmLevel;
    private String alertKey;
    private LocalDateTime utcCreateTime;
}