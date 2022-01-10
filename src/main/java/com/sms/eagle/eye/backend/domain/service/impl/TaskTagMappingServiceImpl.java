package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TaskTagMappingEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskTagMappingMapper;
import com.sms.eagle.eye.backend.domain.service.TaskTagMappingService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class TaskTagMappingServiceImpl extends ServiceImpl<TaskTagMappingMapper, TaskTagMappingEntity>
    implements TaskTagMappingService {

    @Override
    public void updateTagMapping(Long taskId, List<Long> tagList) {
        remove(Wrappers.<TaskTagMappingEntity>lambdaQuery()
            .eq(TaskTagMappingEntity::getTaskId, taskId));
        if (CollectionUtils.isNotEmpty(tagList)) {
            saveBatch(tagList.stream()
                .map(tag -> TaskTagMappingEntity.builder()
                    .taskId(taskId)
                    .tagId(tag)
                    .build())
                .collect(Collectors.toList()));
        }

    }

    @Override
    public List<Long> getTagListByTaskId(Long taskId) {
        return getBaseMapper().getTagListByTaskId(taskId);
    }
}




