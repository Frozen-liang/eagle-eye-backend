package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import java.util.List;

public interface TaskGroupService extends IService<TaskGroupEntity> {

    List<TaskGroupEntity> getEntityList(List<Long> parentIds);

    Integer countByName(String name);

    void saveFromRequest(TaskGroupRequest request, Integer index);

    void updateFromRequest(TaskGroupRequest request, Integer index);

    void rename(Long id, String name);

    Integer getNextIndexByParentId(Long parentId);

    void putAllGroupDown(Long parentId, Integer fromIndex, Integer toIndex);

    void putAllGroupUp(Long parentId, Integer fromIndex, Integer toIndex);

    TaskGroupEntity getEntityById(Long id);

    boolean hasChild(Long id);

    void deleteGroup(Long id);

    void deleteGroupByIds(List<Long> ids);

    List<Long> getChildGroupById(Long id);

    List<Long> getChildGroupByIds(List<Long> ids);


}
