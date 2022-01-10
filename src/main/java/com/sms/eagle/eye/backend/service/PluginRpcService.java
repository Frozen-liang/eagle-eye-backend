package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.domain.entity.TaskEntity;
import com.sms.eagle.eye.backend.response.plugin.PluginMetadataResponse;
import com.sms.eagle.eye.plugin.v1.CreateTaskResponse;
import com.sms.eagle.eye.plugin.v1.GeneralResponse;
import com.sms.eagle.eye.plugin.v1.RegisterResponse;
import java.util.List;

public interface PluginRpcService {

    RegisterResponse getRegisterResponseByTarget(String target);

    PluginMetadataResponse getMetadataResponseByTarget(String target);

    CreateTaskResponse createTask(TaskEntity task, PluginEntity plugin, List<PluginConfigFieldEntity> fields);

    GeneralResponse removeTask(String mappingId, TaskEntity task, PluginEntity plugin,
        List<PluginConfigFieldEntity> fields);

    GeneralResponse updateTask(String mappingId, TaskEntity task, PluginEntity plugin,
        List<PluginConfigFieldEntity> fields);
}
