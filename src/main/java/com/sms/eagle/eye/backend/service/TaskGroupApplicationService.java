package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.response.task.TaskGroupTreeResponse;
import java.util.List;

public interface TaskGroupApplicationService {

    /**
     * 根据 父节点id 获取所有的子节点
     *
     * <p>只会获取相邻的子节点，即只往下查找一级.
     *
     * @param parentId 父节点id
     */
    List<TaskGroupResponse> getGroupListByParentId(Long parentId);

    /**
     * 获取所有的任务分组，以树的形式返回.
     */
    List<TaskGroupTreeResponse> getTaskGroupTreeList();

    /**
     * 添加任务组.
     */
    boolean addGroup(TaskGroupRequest request);

    /**
     * 改变任务组的顺序以及上下级关系.
     */
    boolean updateGroup(TaskGroupRequest request);

    /**
     * 改变任务组名称.
     */
    boolean rename(TaskGroupRequest request);

    /**
     * 删除任务组.
     */
    boolean removeGroup(Long id);
}
