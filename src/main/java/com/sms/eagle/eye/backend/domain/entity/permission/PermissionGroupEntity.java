package com.sms.eagle.eye.backend.domain.entity.permission;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sms.eagle.eye.backend.common.handler.SetTypeHandler;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

@TableName(value = "permission_group")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupEntity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    private String name;
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = SetTypeHandler.class)
    @Default
    private Set<String> permissions = new HashSet<>();
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;


}
