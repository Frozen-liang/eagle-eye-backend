package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.domain.service.impl.TaskGroupServiceImpl.INDEX_STEP;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GROUP_NAME_HAS_ALREADY_EXIST;
import static com.sms.eagle.eye.backend.exception.ErrorCode.REMOVE_CHILD_BEFORE_DELETE_GROUP;

import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.service.TaskGroupApplicationService;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TaskGroupApplicationServiceImpl implements TaskGroupApplicationService {

    private final TaskGroupService taskGroupService;

    public TaskGroupApplicationServiceImpl(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    @Override
    public List<TaskGroupResponse> getTreeList() {
        return taskGroupService.getTreeList();
    }

    /**
     * 验证是否有重名
     * 添加至同级节点的最后一个.
     */
    @Override
    public boolean addGroup(TaskGroupRequest request) {
        if (taskGroupService.countByName(request.getName()) != 0) {
            throw new EagleEyeException(GROUP_NAME_HAS_ALREADY_EXIST);
        }
        taskGroupService.saveFromRequest(request, taskGroupService.getNextIndexByParentId(request.getParentId()));
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGroup(TaskGroupRequest request) {
        TaskGroupEntity entity = taskGroupService.getEntityById(request.getId());
        if (Objects.equals(entity.getParentId(), request.getParentId())) {
            taskGroupService.moveGroupBack(request.getParentId(), request.getPreIndex(), entity.getIndex());
        } else {
            taskGroupService.moveGroupBack(request.getParentId(), request.getPreIndex(), null);
        }
        taskGroupService.updateFromRequest(request, request.getPreIndex() + INDEX_STEP);
        return true;
    }

    /**
     * 检查是否有子节点.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGroup(Long id) {
        TaskGroupEntity entity = taskGroupService.getEntityById(id);
        if (taskGroupService.hasChild(id)) {
            throw new EagleEyeException(REMOVE_CHILD_BEFORE_DELETE_GROUP);
        }
        taskGroupService.deleteGroup(id);
        taskGroupService.moveGroupForward(entity.getParentId(), entity.getIndex());
        return true;
    }
}