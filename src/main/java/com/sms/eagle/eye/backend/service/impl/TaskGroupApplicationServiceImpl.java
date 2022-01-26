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

    /**
     * 情况一：
     *  从同组下方移动到上方
     * 情况二：
     *  从同组上方移动到下方
     * 情况三：
     *  移动到其他组.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGroup(TaskGroupRequest request) {
        TaskGroupEntity entity = taskGroupService.getEntityById(request.getId());
        Integer index = 0;
        if (inSameLevel(request, entity)) {
            if (isMoveUp(request, entity)) {
                // 大于 preIndex，小于 node index 的所有节点向下移.
                taskGroupService.putAllGroupDown(request.getParentId(), request.getPreIndex(), entity.getIndex());
                index = request.getPreIndex() + INDEX_STEP;
            } else if (isMoveDown(request, entity)){
                // 大于 node index，小于 preIndex + INDEX_STEP 的所有节点向上移.
                taskGroupService.putAllGroupUp(
                    request.getParentId(), entity.getIndex(),request.getPreIndex() + INDEX_STEP);
                index = request.getPreIndex();
            }
        } else {
            // 原先层级大于node index的节点向上移.
            taskGroupService.putAllGroupUp(entity.getParentId(), entity.getIndex(), null);
            // 新层级大于preIndex的所有节点向下移.
            taskGroupService.putAllGroupDown(request.getParentId(), request.getPreIndex(), null);
            index = request.getPreIndex() + INDEX_STEP;
        }
        taskGroupService.updateFromRequest(request, index);
        return true;
    }

    private boolean inSameLevel(TaskGroupRequest request, TaskGroupEntity entity) {
        return Objects.equals(request.getParentId(), entity.getParentId());
    }

    private boolean isMoveUp(TaskGroupRequest request, TaskGroupEntity entity) {
        return entity.getIndex().compareTo(request.getPreIndex() + INDEX_STEP) > 0;
    }

    private boolean isMoveDown(TaskGroupRequest request, TaskGroupEntity entity) {
        return entity.getIndex().compareTo(request.getPreIndex() + INDEX_STEP) < 0;
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
        taskGroupService.putAllGroupUp(entity.getParentId(), entity.getIndex(), null);
        return true;
    }
}