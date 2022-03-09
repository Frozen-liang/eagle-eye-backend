package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import com.sms.eagle.eye.backend.response.task.TaskGroupTreeResponse;
import java.util.List;

public interface TaskGroupApplicationService {

    List<TaskGroupResponse> getGroupListByParentId(Long parentId);

    List<TaskGroupTreeResponse> getTaskGroupTreeList();

    boolean addGroup(TaskGroupRequest request);

    boolean updateGroup(TaskGroupRequest request);

    boolean rename(TaskGroupRequest request);

    boolean removeGroup(Long id);
}
