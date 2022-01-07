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

@TableName(value = "plugin")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private String name;
    private String url;
    private String description;
    private Integer version;
    @TableField("is_schedule_by_self")
    private Boolean scheduleBySelf;
    private String creator;
    @TableField("is_enabled")
    private Boolean enabled;
    private LocalDateTime utcCreateTime;
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}