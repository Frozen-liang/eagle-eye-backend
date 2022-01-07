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
public class PluginDetailResponse {

    /**
     * 配置字段.
     */
    private List<PluginConfigFieldResponse> fields;
    /**
     * 选择数据.
     */
    private List<PluginSelectOptionResponse> options;
}