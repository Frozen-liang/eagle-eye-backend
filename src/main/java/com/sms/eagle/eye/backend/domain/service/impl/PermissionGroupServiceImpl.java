package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.PermissionGroupConverter;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupEntity;
import com.sms.eagle.eye.backend.domain.mapper.PermissionGroupMapper;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupService;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupQueryRequest;
import com.sms.eagle.eye.backend.request.permission.PermissionGroupRequest;
import com.sms.eagle.eye.backend.response.permission.PermissionGroupResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class PermissionGroupServiceImpl extends ServiceImpl<PermissionGroupMapper, PermissionGroupEntity> implements
    PermissionGroupService {

    private final PermissionGroupConverter permissionGroupConverter;

    public PermissionGroupServiceImpl(PermissionGroupConverter permissionGroupConverter) {
        this.permissionGroupConverter = permissionGroupConverter;
    }

    @Override
    public IPage<PermissionGroupResponse> page(PermissionGroupQueryRequest pageRequest) {
        return getBaseMapper().page(pageRequest.getPageInfo(), pageRequest);
    }

    @Override
    public Long saveFromRequest(PermissionGroupRequest request) {
        PermissionGroupEntity permissionGroup = permissionGroupConverter.toEntity(request);
        save(permissionGroup);
        return permissionGroup.getId();
    }

    @Override
    public boolean updateFromRequest(PermissionGroupRequest request) {
        PermissionGroupEntity permissionGroup = permissionGroupConverter.toEntity(request);
        return updateById(permissionGroup);
    }

    @Override
    public boolean deleteById(Long id) {
        return removeById(id);
    }

    @Override
    public List<PermissionGroupResponse> queryAll() {
        LambdaQueryWrapper<PermissionGroupEntity> queryWrapper = Wrappers
            .lambdaQuery(PermissionGroupEntity.class)
            .select(PermissionGroupEntity::getId, PermissionGroupEntity::getName);
        return permissionGroupConverter.toResponses(list(queryWrapper));
    }

    @Override
    public PermissionGroupEntity getOne(Long id) {
        return getById(id);
    }
}
