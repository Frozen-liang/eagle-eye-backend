package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.enums.SelectData;
import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/data")
public class DataController {

    private final DataApplicationService dataApplicationService;

    public DataController(DataApplicationService dataApplicationService) {
        this.dataApplicationService = dataApplicationService;
    }

    @GetMapping("/config")
    public Response<Map<String, Object>> getSelectConfig() {
        return Response.ok(SelectData.getSelectData(dataApplicationService));
    }

}