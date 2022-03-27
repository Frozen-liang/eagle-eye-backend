package com.sms.eagle.eye.backend.nerko.client;

import com.sms.eagle.eye.backend.nerko.config.NerkoFeignConfiguration;
import com.sms.eagle.eye.backend.nerko.dto.NerkoProjectInfo;
import com.sms.eagle.eye.backend.nerko.response.NerkoBaseResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "coordination", url = "${nerko.resource.base-path:https://nerkogw.smsassist.com}",
    path = "coordination", configuration = NerkoFeignConfiguration.class)
public interface CoordinationClient {

    /**
     * 获取项目列表.
     */
    @GetMapping("/v1/project-info/list")
    NerkoBaseResponse<List<NerkoProjectInfo>> getProjectList();
}
