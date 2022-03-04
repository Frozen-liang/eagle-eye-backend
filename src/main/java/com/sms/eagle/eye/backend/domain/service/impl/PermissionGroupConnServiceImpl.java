package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.domain.entity.permission.PermissionGroupConnEntity;
import com.sms.eagle.eye.backend.domain.mapper.PermissionGroupConnMapper;
import com.sms.eagle.eye.backend.domain.service.PermissionGroupConnService;
import org.springframework.stereotype.Service;

@Service
public class PermissionGroupConnServiceImpl extends
    ServiceImpl<PermissionGroupConnMapper, PermissionGroupConnEntity> implements PermissionGroupConnService {

}
