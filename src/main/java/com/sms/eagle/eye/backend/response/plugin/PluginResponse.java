package com.sms.eagle.eye.backend.response.plugin;

import static com.sms.eagle.eye.backend.common.TimePatternConstant.DATE_TIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PluginResponse {

    /**
     * 唯一id.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 插件服务路径.
     */
    private String url;
    /**
     * 插件名称.
     */
    private String name;
    /**
     * 插件描述.
     */
    private String description;
    /**
     * 插件版本.
     */
    private Integer version;
    private Boolean scheduleBySelf;
    /**
     * 创建人.
     */
    private String creator;
    /**
     * 是否开启.
     */
    private Boolean enabled;
    /**
     * 创建时间.
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createTime;

}