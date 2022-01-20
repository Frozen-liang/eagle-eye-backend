package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.request.group.TaskGroupRequest;
import com.sms.eagle.eye.backend.response.task.TaskGroupResponse;
import java.util.List;

public interface TaskGroupApplicationService {

    List<TaskGroupResponse> getTreeList();

    boolean addGroup(TaskGroupRequest request);

    boolean updateGroup(TaskGroupRequest request);

    boolean removeGroup(Long id);
}
