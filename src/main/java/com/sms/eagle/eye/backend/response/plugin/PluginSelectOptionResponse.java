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
public class PluginSelectOptionResponse {

    private String key;
    private List<PluginSelectOptionItemResponse> data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PluginSelectOptionItemResponse {

        private String label;
        private String value;
    }
}