package com.sms.eagle.eye.backend.domain.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "permission_group_conn")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupConnEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    @TableField("group_id")
    private Long groupId;
    @TableField("permission_id")
    private Long permissionId;
}
