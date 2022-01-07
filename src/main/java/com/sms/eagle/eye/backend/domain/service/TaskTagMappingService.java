package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskTagMappingEntity;
import java.util.List;

public interface TaskTagMappingService extends IService<TaskTagMappingEntity> {

    void updateTagMapping(Long taskId, List<Long> tagList);

    List<Long> getTagListByTaskId(Long taskId);
}
