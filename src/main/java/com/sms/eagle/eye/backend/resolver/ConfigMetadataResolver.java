package com.sms.eagle.eye.backend.resolver;

import com.sms.eagle.eye.backend.model.ConfigMetadata;
import java.util.List;
import java.util.Map;

public interface ConfigMetadataResolver {

    Map<String, Object> convertConfigToMap(String config);

    /**
     * 检查输入配置是否符合元数据的要求
     * 并进行一些特殊处理.
     */
    String checkAndEncrypt(List<ConfigMetadata> metadataList, String config);

    Object decryptToFrontendValue(ConfigMetadata metadata, Map<String, Object> configMap);

    String decryptToString(List<ConfigMetadata> metadataList, String config);
}
