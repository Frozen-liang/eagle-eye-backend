package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.common.enums.TaskStatus;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.response.task.TaskResponse;
import java.util.List;
import java.util.Optional;

public interface TaskService extends IService<TaskEntity> {

    IPage<TaskResponse> getPage(TaskQueryRequest request, List<Long> groups);

    Long saveFromRequest(TaskBasicInfoRequest request);

    void updateFromRequest(TaskBasicInfoRequest request);

    TaskEntity getEntityById(Long id);

    void updateTaskEntity(TaskEntity taskEntity);

    void deleteTaskById(Long taskId);

    Optional<TaskEntity> getEntityByName(String name);

    Integer countByName(String name);

    TaskStatus getTaskStatusById(Long taskId);
}
