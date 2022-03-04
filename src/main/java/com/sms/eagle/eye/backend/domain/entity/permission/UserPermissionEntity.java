package com.sms.eagle.eye.backend.domain.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "user_permission")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionEntity implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private String email;
    @TableField("permission_group_id")
    private Long permissionGroupId;
}
