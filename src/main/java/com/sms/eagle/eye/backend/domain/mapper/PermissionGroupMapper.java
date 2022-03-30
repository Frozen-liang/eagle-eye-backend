package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PermissionGroupMapper extends BaseMapper<PermissionGroupEntity> {

    IPage<PermissionGroupResponse> page(IPage<?> page,
        @Param("request") PermissionGroupQueryRequest request);
}
