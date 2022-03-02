package com.sms.eagle.eye.backend.resolver;

import com.sms.eagle.eye.backend.model.ConfigMetadata;
import java.util.List;
import java.util.Map;

public interface ConfigMetadataResolver {

    Map<String, String> convertConfigToMap(String config);

    /**
     * 检查输入配置是否符合元数据的要求
     * 并进行一些特殊处理（加密等）.
     */
    String checkAndEncrypt(List<ConfigMetadata> metadataList, String config, String oldConfig);

    String decryptToFrontendValue(ConfigMetadata metadata, Map<String, String> configMap);

    String decryptToString(List<ConfigMetadata> metadataList, String config);
}
