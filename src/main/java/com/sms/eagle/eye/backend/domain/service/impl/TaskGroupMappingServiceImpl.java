package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskGroupMappingMapper;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@DomainServiceAdvice
public class TaskGroupMappingServiceImpl extends ServiceImpl<TaskGroupMappingMapper, TaskGroupMappingEntity>
    implements TaskGroupMappingService {

    @Override
    public void updateGroupMapping(Long taskId, List<Long> groupList) {
        remove(Wrappers.<TaskGroupMappingEntity>lambdaQuery()
            .eq(TaskGroupMappingEntity::getTaskId, taskId));
        if (CollectionUtils.isNotEmpty(groupList)) {
            saveBatch(groupList.stream()
                .map(group -> TaskGroupMappingEntity.builder()
                    .taskId(taskId)
                    .groupId(group)
                    .build())
                .collect(Collectors.toList()));
        }
    }

    @Override
    public List<Long> getGroupListByTaskId(Long taskId) {
        return getBaseMapper().getGroupListByTaskId(taskId);
    }
}




