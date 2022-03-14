package com.sms.eagle.eye.backend.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertRuleEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskAlertRuleMapper;
import com.sms.eagle.eye.backend.domain.service.TaskAlertRuleService;
import com.sms.eagle.eye.backend.request.task.TaskAlertRuleRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class TaskAlertRuleServiceImpl extends ServiceImpl<TaskAlertRuleMapper, TaskAlertRuleEntity>
    implements TaskAlertRuleService {

    private static final String LIMIT_1 = "limit 1";

    @Override
    public Optional<TaskAlertRuleEntity> getByTaskIdAndAlertLevel(Long taskId, Integer alertLevel) {
        return Optional.ofNullable(getOne(Wrappers.<TaskAlertRuleEntity>lambdaQuery()
            .eq(TaskAlertRuleEntity::getTaskId, taskId)
            .eq(TaskAlertRuleEntity::getAlarmLevel, alertLevel).last(LIMIT_1)));
    }


    @Override
    public void updateByRequest(TaskAlertRuleRequest request) {
        TaskAlertRuleEntity entity = TaskAlertRuleEntity.builder().build();
        BeanUtils.copyProperties(request, entity);
        Optional<TaskAlertRuleEntity> entityOptional = getByTaskIdAndAlertLevel(
            request.getTaskId(), request.getAlarmLevel());
        entityOptional.ifPresent(taskAlertRuleEntity -> entity.setId(taskAlertRuleEntity.getId()));
        saveOrUpdate(entity);
    }

    @Override
    public List<TaskAlertRuleEntity> getByTaskId(Long taskId) {
        return list(Wrappers.<TaskAlertRuleEntity>lambdaQuery().eq(TaskAlertRuleEntity::getTaskId, taskId));
    }

    @Override
    public Optional<TaskAlertRuleEntity> getByTaskAlertRuleId(Long taskAlertRuleId) {
        if (Objects.isNull(taskAlertRuleId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(getById(taskAlertRuleId));
    }
}




