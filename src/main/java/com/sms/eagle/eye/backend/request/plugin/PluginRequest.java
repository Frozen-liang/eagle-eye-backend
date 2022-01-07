package com.sms.eagle.eye.backend.request.plugin;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginRequest {

    /**
     * 插件服务地址.
     */
    @NotNull
    private String url;
}