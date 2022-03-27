package com.sms.eagle.eye.backend.nerko.client;

import com.sms.eagle.eye.backend.nerko.config.NerkoFeignConfiguration;
import com.sms.eagle.eye.backend.nerko.dto.NerkoUserInfo;
import com.sms.eagle.eye.backend.nerko.response.NerkoBaseResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-management", url = "${nerko.resource.base-path:https://nerkogw.smsassist.com}",
    path = "user-management", configuration = NerkoFeignConfiguration.class)
public interface UserManagementClient {

    @GetMapping("/v1/user")
    NerkoBaseResponse<List<NerkoUserInfo>> getUserList();
}
