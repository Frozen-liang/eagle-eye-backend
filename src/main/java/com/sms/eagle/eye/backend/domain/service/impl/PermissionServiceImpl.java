package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PermissionConverter;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionEntity;
import com.sms.eagle.eye.backend.domain.mapper.PermissionMapper;
import com.sms.eagle.eye.backend.domain.service.PermissionService;
import com.sms.eagle.eye.backend.response.permission.PermissionResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements
    PermissionService {

    private final PermissionConverter permissionConverter;

    public PermissionServiceImpl(PermissionConverter permissionConverter) {
        this.permissionConverter = permissionConverter;
    }

    @Override
    public List<PermissionResponse> listByName(String name) {
        LambdaQueryWrapper<PermissionEntity> queryWrapper = Wrappers.lambdaQuery(PermissionEntity.class)
            .like(StringUtils.isNotBlank(name), PermissionEntity::getName, name);
        return permissionConverter.toResponses(list(queryWrapper));
    }
}
