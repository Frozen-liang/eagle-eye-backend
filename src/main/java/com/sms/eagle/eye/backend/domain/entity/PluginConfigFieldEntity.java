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

@TableName(value = "plugin_config_field")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginConfigFieldEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long pluginId;
    private String key;
    private String labelName;
    private Integer type;
    private String option;
    private String defaultValue;
    @TableField("is_required")
    private Boolean required;
    @TableField("is_encrypted")
    private Boolean encrypted;
    @TableField("display_order")
    private Integer order;
    private LocalDateTime utcCreateTime;
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}