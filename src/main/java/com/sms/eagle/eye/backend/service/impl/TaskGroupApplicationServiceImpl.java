package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.domain.service.impl.TaskGroupServiceImpl.INDEX_STEP;
import static com.sms.eagle.eye.backend.domain.service.impl.TaskGroupServiceImpl.ROOT_ID;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GROUP_NAME_HAS_ALREADY_EXIST;
import static com.sms.eagle.eye.backend.exception.ErrorCode.REMOVE_CHILD_BEFORE_DELETE_GROUP;
import static com.sms.eagle.eye.backend.exception.ErrorCode.REMOVE_TASK_BEFORE_DELETE_GROUP;

import com.sms.eagle.eye.backend.common.function.FunctionHandler;
import com.sms.eagle.eye.backend.domain.entity.TaskGroupEntity;
import com.sms.eagle.eye.backend.domain.service.TaskGroupMappingService;
import com.sms.eagle.eye.backend.domain.service.TaskGroupService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.response.task.TaskGroupTreeResponse;
import com.sms.eagle.eye.backend.service.TaskGroupApplicationService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TaskGroupApplicationServiceImpl implements TaskGroupApplicationService {

    private final TaskGroupService taskGroupService;
    private final TaskGroupMappingService taskGroupMappingService;

    public TaskGroupApplicationServiceImpl(TaskGroupService taskGroupService,
        TaskGroupMappingService taskGroupMappingService) {
        this.taskGroupService = taskGroupService;
        this.taskGroupMappingService = taskGroupMappingService;
    }

    /**
     * 根据 父节点id 获取所有相邻的子节点.
     *
     * @param parentId 父节点id
     */
    @Override
    public List<TaskGroupResponse> getGroupListByParentId(Long parentId) {
        if (Objects.isNull(parentId)) {
            parentId = ROOT_ID;
        }
        List<TaskGroupEntity> entities = taskGroupService.getEntityList(List.of(parentId));
        List<Long> ids = entities.stream().map(TaskGroupEntity::getId).collect(Collectors.toList());
        List<TaskGroupEntity> childLists = taskGroupService.getEntityList(ids);
        entities.addAll(childLists);
        Map<Long, List<TaskGroupEntity>> map = entities.stream()
            .collect(Collectors.groupingBy(TaskGroupEntity::getParentId));
        return getList(parentId, map);
    }

    /**
     * 获取所有的任务分组，以树的形式返回.
     */
    @Override
    public List<TaskGroupTreeResponse> getTaskGroupTreeList() {
        Map<Long, List<TaskGroupEntity>> map = taskGroupService.getEntityList(null).stream()
            .collect(Collectors.groupingBy(TaskGroupEntity::getParentId));
        return getTreeList(ROOT_ID, map);
    }

    /**
     * 根据 父节点id 得到其所有相邻子节点的数据.
     *
     * @param id 父节点id
     * @param map 父节点及其对应子节点的哈希表
     */
    private List<TaskGroupResponse> getList(Long id, Map<Long, List<TaskGroupEntity>> map) {
        List<TaskGroupEntity> entities = map.get(id);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
            .sorted(Comparator.comparing(TaskGroupEntity::getIndex))
            .map(entity -> convertToListResponse(entity, map))
            .collect(Collectors.toList());
    }

    /**
     * 根据 父节点id 得到其所有子节点的数据.
     *
     * @param id 父节点id
     * @param map 父节点及其对应子节点的哈希表
     */
    private List<TaskGroupTreeResponse> getTreeList(Long id, Map<Long, List<TaskGroupEntity>> map) {
        List<TaskGroupEntity> entities = map.get(id);
        if (CollectionUtils.isEmpty(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
            .sorted(Comparator.comparing(TaskGroupEntity::getIndex))
            .map(entity -> convertToTreeResponse(entity, map))
            .collect(Collectors.toList());
    }

    private TaskGroupResponse convertToListResponse(TaskGroupEntity entity, Map<Long, List<TaskGroupEntity>> map) {
        return TaskGroupResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .index(entity.getIndex())
            .isLeaf(CollectionUtils.isEmpty(map.get(entity.getId())))
            .build();
    }

    /**
     * 将 {@link TaskGroupEntity} 转化为 {@link TaskGroupTreeResponse}.
     */
    private TaskGroupTreeResponse convertToTreeResponse(TaskGroupEntity entity, Map<Long, List<TaskGroupEntity>> map) {
        return TaskGroupTreeResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .index(entity.getIndex())
            .child(getTreeList(entity.getId(), map))
            .build();
    }

    /**
     * TODO 加锁 添加任务组.
     *
     * <p>需要验证是否有重名,
     *
     * <p>新增节点要添加至同级节点的最后一个.
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
     * TODO 并发分析 改变任务组的顺序以及上下级关系.
     *
     * <p>情况一： 从同组下方移动到上方
     *
     * <p>情况二： 从同组上方移动到下方
     *
     * <p>情况三： 移动到其他组.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized boolean updateGroup(TaskGroupRequest request) {
        TaskGroupEntity entity = taskGroupService.getEntityById(request.getId());
        Integer index = 0;
        if (inSameLevel(request, entity)) {
            if (isMoveUp(request, entity)) {
                // 大于 preIndex，小于 node index 的所有节点向下移.
                taskGroupService.putAllGroupDown(request.getParentId(), request.getPreIndex(), entity.getIndex());
                index = request.getPreIndex() + INDEX_STEP;
            } else if (isMoveDown(request, entity)) {
                // 大于 node index，小于 preIndex + INDEX_STEP 的所有节点向上移.
                taskGroupService.putAllGroupUp(
                    request.getParentId(), entity.getIndex(), request.getPreIndex() + INDEX_STEP);
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

    /**
     * 更改任务分组名称.
     */
    @Override
    public boolean rename(TaskGroupRequest request) {
        if (taskGroupService.countByName(request.getName()) != 0) {
            throw new EagleEyeException(GROUP_NAME_HAS_ALREADY_EXIST);
        }
        taskGroupService.rename(request.getId(), request.getName());
        return true;
    }

    /**
     * 判断修改前与修改后是否在同一级.
     *
     * @param request 任务分组修改数据
     * @param entity 任务分组原始数据
     */
    private boolean inSameLevel(TaskGroupRequest request, TaskGroupEntity entity) {
        return Objects.equals(request.getParentId(), entity.getParentId());
    }

    /**
     * 判断修改前与修改后是否是向上移动.
     */
    private boolean isMoveUp(TaskGroupRequest request, TaskGroupEntity entity) {
        return entity.getIndex().compareTo(request.getPreIndex() + INDEX_STEP) > 0;
    }

    /**
     * 判断修改前与修改后是否是向下移动.
     */
    private boolean isMoveDown(TaskGroupRequest request, TaskGroupEntity entity) {
        return entity.getIndex().compareTo(request.getPreIndex() + INDEX_STEP) < 0;
    }

    /**
     * 检查是否有子节点,有则不允许删除.
     * 检查是否有任务,有则不允许删除.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGroup(Long id) {
        FunctionHandler.isTrue(taskGroupService.hasChild(id)).throwMessage(REMOVE_CHILD_BEFORE_DELETE_GROUP);
        FunctionHandler.isTrue(taskGroupMappingService.countByGroupId(id) > 0)
            .throwMessage(REMOVE_TASK_BEFORE_DELETE_GROUP);
        TaskGroupEntity entity = taskGroupService.getEntityById(id);
        taskGroupService.deleteGroup(id);
        taskGroupService.putAllGroupUp(entity.getParentId(), entity.getIndex(), null);
        return Boolean.TRUE;
    }

    /**
     * 检查是否有子节点,有则不允许删除.
     * 检查是否有任务,有则不允许删除.
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGroupByIds(List<Long> ids) {
        List<TaskGroupEntity> entityList = taskGroupService.listByIds(ids);
        List<Long> childIds = taskGroupService.getChildGroupByIds(ids);
        Map<Long, TaskGroupEntity> entityMap = entityList.stream().collect(Collectors.toMap(TaskGroupEntity::getId,
            Function.identity()));
        List<Long> noExistIds = childIds.stream().filter(id -> !entityMap.containsKey(id)).collect(Collectors.toList());
        FunctionHandler.isTrue(CollectionUtils.isNotEmpty(noExistIds)).throwMessage(REMOVE_CHILD_BEFORE_DELETE_GROUP);
        FunctionHandler.isTrue(taskGroupMappingService.countByGroupIds(ids) > 0)
            .throwMessage(REMOVE_TASK_BEFORE_DELETE_GROUP);
        for (TaskGroupEntity groupEntity : entityList) {
            TaskGroupEntity entity = taskGroupService.getEntityById(groupEntity.getId());
            taskGroupService.deleteGroup(entity.getId());
            taskGroupService.putAllGroupUp(entity.getParentId(), entity.getIndex(), null);
        }
        return Boolean.TRUE;
    }

}