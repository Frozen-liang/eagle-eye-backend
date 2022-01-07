package com.sms.eagle.eye.backend.request.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sms.eagle.eye.backend.utils.KeepAsJsonDeserializer;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPluginConfigRequest {

    /**
     * 任务id.
     */
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 插件配置.
     */
    @NotNull
    @JsonDeserialize(using = KeepAsJsonDeserializer.class)
    private String pluginConfig;
}