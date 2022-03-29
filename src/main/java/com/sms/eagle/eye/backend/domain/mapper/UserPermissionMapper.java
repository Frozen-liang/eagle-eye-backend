package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.permission.UserPermissionGroupEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPermissionMapper extends BaseMapper<UserPermissionGroupEntity> {

    String getPermissionByEmail(String email);

}
