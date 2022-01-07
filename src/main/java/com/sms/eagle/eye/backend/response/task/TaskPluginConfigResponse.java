package com.sms.eagle.eye.backend.response.task;

import com.sms.eagle.eye.backend.response.plugin.PluginSelectOptionResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPluginConfigResponse {

    private List<PluginConfigFieldWithValueResponse> fields;
    private List<PluginSelectOptionResponse> options;
}