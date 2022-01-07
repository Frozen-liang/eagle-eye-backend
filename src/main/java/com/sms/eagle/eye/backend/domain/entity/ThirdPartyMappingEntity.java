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

@TableName(value = "third_party_mapping")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyMappingEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private Long taskId;
    private String mappingId;
    private Integer type;
    private LocalDateTime utcCreateTime;
}