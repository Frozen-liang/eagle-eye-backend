package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_ALERT_NOTIFICATION_ID_IS_NOT_CORRECT;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.domain.entity.TaskAlertNotificationEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskAlertNotificationMapper;
import com.sms.eagle.eye.backend.domain.service.TaskAlertNotificationService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationAddRequest;
import com.sms.eagle.eye.backend.request.task.TaskAlertNotificationUpdateRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class TaskAlertNotificationServiceImpl extends
    ServiceImpl<TaskAlertNotificationMapper, TaskAlertNotificationEntity> implements TaskAlertNotificationService {

    @Override
    public TaskAlertNotificationEntity getEntityById(Long alertNotificationId) {
        return Optional.ofNullable(getById(alertNotificationId))
            .orElseThrow(() -> new EagleEyeException(TASK_ALERT_NOTIFICATION_ID_IS_NOT_CORRECT));
    }

    @Override
    public List<TaskAlertNotificationEntity> getByTaskIdAndAlarmLevel(Long taskId, Integer alarmLevel) {
        return list(Wrappers.<TaskAlertNotificationEntity>lambdaQuery()
            .eq(TaskAlertNotificationEntity::getTaskId, taskId)
            .eq(TaskAlertNotificationEntity::getAlarmLevel, alarmLevel));
    }

    @Override
    public void addByRequest(TaskAlertNotificationAddRequest request) {
        TaskAlertNotificationEntity entity = TaskAlertNotificationEntity.builder().build();
        BeanUtils.copyProperties(request, entity);
        save(entity);
    }

    @Override
    public void updateByRequest(TaskAlertNotificationUpdateRequest request) {
        update(Wrappers.<TaskAlertNotificationEntity>lambdaUpdate()
            .eq(TaskAlertNotificationEntity::getId, request.getAlertNotificationId())
            .set(TaskAlertNotificationEntity::getChannelInput, request.getChannelInput())
            .set(TaskAlertNotificationEntity::getContent, request.getContent()));
    }

    @Override
    public void delete(Long alertNotificationId) {
        removeById(alertNotificationId);
    }
}




