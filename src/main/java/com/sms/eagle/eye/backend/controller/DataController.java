package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.enums.SelectData;
import com.sms.eagle.eye.backend.nerko.service.NerkoCoordinationService;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.service.DataApplicationService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/data")
public class DataController {

    private final DataApplicationService dataApplicationService;
    private final NerkoCoordinationService nerkoCoordinationService;

    public DataController(DataApplicationService dataApplicationService,
        NerkoCoordinationService nerkoCoordinationService) {
        this.dataApplicationService = dataApplicationService;
        this.nerkoCoordinationService = nerkoCoordinationService;
    }

    @GetMapping("/config")
    public Response<Map<String, Object>> getSelectConfig() {
        return Response.ok(SelectData.getSelectData(dataApplicationService));
    }

    @GetMapping("/projects")
    public Response<List<String>> getProjects() {
        return Response.ok(nerkoCoordinationService.getProjectList());
    }

}