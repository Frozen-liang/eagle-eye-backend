package com.sms.eagle.eye.backend.resolver;

import com.sms.eagle.eye.backend.domain.entity.PluginConfigFieldEntity;
import com.sms.eagle.eye.backend.response.task.PluginConfigFieldWithValueResponse;
import java.util.List;

public interface PluginConfigResolver {

    String checkAndEncrypt(List<PluginConfigFieldEntity> fields, String config, String oldConfig);

    List<PluginConfigFieldWithValueResponse> decryptToResponse(List<PluginConfigFieldEntity> fields, String config);

    String decryptToString(List<PluginConfigFieldEntity> fields, String config);
}