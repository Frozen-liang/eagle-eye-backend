package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.request.task.TaskBasicInfoRequest;
import com.sms.eagle.eye.backend.request.task.TaskQueryRequest;
import com.sms.eagle.eye.backend.response.task.TaskResponse;

public interface TaskService extends IService<TaskEntity> {

    IPage<TaskResponse> getPage(TaskQueryRequest request);

    Long saveFromRequest(TaskBasicInfoRequest request);

    void updateFromRequest(TaskBasicInfoRequest request);

    TaskEntity getEntityById(Long id);

    void updateTaskEntity(TaskEntity taskEntity);

    void deleteTaskById(Long taskId);
}
