package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import java.util.List;

public interface TaskGroupService extends IService<TaskGroupEntity> {

    List<TaskGroupResponse> getTreeList();

    Integer countByName(String name);

    void saveFromRequest(TaskGroupRequest request, Integer index);

    void updateFromRequest(TaskGroupRequest request, Integer index);

    Integer getNextIndexByParentId(Long parentId);

    void moveGroupBack(Long parentId, Integer fromIndex, Integer toIndex);

    void moveGroupForward(Long parentId, Integer fromIndex);

    TaskGroupEntity getEntityById(Long id);

    boolean hasChild(Long id);

    void deleteGroup(Long id);

    List<Long> getChildGroupById(Long id);

}
