package com.sms.eagle.eye.backend.request.plugin;

import com.sms.eagle.eye.backend.domain.entity.PluginEntity;
import com.sms.eagle.eye.backend.request.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginQueryRequest extends PageRequest<PluginEntity> {

    /**
     * 插件名称.
     */
    private String name;
    /**
     * 创建人.
     */
    private String creator;
}