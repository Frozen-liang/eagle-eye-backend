package com.sms.eagle.eye.backend.response.plugin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginMetadataResponse {

    /**
     * 插件名称.
     */
    private String name;
    /**
     * 描述.
     */
    private String description;
    /**
     * 插件版本.
     */
    private Integer version;
    /**
     * 配置字段.
     */
    private List<PluginConfigFieldResponse> fields;
    /**
     * 选择数据.
     */
    private List<PluginSelectOptionResponse> options;
    private Boolean scheduleBySelf;
}