package com.sms.eagle.eye.backend.handler;

import com.sms.eagle.eye.backend.request.task.TaskOperationRequest;

public interface TaskHandler {

    void startTask(TaskOperationRequest request);

    void stopTask(TaskOperationRequest request);

    void updateTask(TaskOperationRequest request);

    Boolean ifScheduleBySelf();
}
