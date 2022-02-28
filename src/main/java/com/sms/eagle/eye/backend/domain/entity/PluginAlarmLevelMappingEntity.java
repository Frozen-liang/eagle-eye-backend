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

@TableName(value = "plugin_alarm_level_mapping")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginAlarmLevelMappingEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long pluginId;
    private String systemLevel;
    private String mappingLevel;

    private LocalDateTime utcCreateTime;
}