package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupMappingEntity;
import java.util.List;

public interface TaskGroupMappingService extends IService<TaskGroupMappingEntity> {

    void updateGroupMapping(Long taskId, List<Long> groupList);

    List<Long> getGroupListByTaskId(Long taskId);

    Integer countByGroupId(Long groupId);

    Integer countByGroupIds(List<Long> groupIds);
}
