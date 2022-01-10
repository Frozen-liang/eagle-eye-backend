package com.sms.eagle.eye.backend.domain.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.TASK_ID_IS_NOT_CORRECT;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sms.eagle.eye.backend.aspect.DomainServiceAdvice;
import com.sms.eagle.eye.backend.convert.TaskConverter;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.domain.mapper.TaskMapper;
import com.sms.eagle.eye.backend.domain.service.TaskService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import com.sms.eagle.eye.backend.utils.SecurityUtil;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@DomainServiceAdvice
public class TaskServiceImpl extends ServiceImpl<TaskMapper, TaskEntity>
    implements TaskService {

    private final TaskConverter taskConverter;

    public TaskServiceImpl(TaskConverter taskConverter) {
        this.taskConverter = taskConverter;
    }

    @Override
    public IPage<TaskResponse> getPage(TaskQueryRequest request) {
        return getBaseMapper().getPage(request.getPageInfo(), request);
    }

    @Override
    public Long saveFromRequest(TaskBasicInfoRequest request) {
        TaskEntity taskEntity = taskConverter.toEntity(request);
        taskEntity.setCreator(SecurityUtil.getCurrentUser().getUsername());
        save(taskEntity);
        return taskEntity.getId();
    }

    @Override
    public void updateFromRequest(TaskBasicInfoRequest request) {
        updateById(taskConverter.toEntity(request));
    }

    @Override
    public TaskEntity getEntityById(Long id) {
        Optional<TaskEntity> taskOptional = Optional.ofNullable(getById(id));
        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new EagleEyeException(TASK_ID_IS_NOT_CORRECT);
    }

    @Override
    public void updateTaskEntity(TaskEntity taskEntity) {
        updateById(taskEntity);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        removeById(taskId);
    }

    @Override
    public Optional<Long> getIdByName(String name) {
        return getBaseMapper().getIdByName(name);
    }
}




