package com.sms.eagle.eye.backend.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "password_store")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordStoreEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private String key;
    private String value;
    private String description;
    private String creator;
    private LocalDateTime utcCreateTime;
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;
}