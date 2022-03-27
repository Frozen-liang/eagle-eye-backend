package com.sms.eagle.eye.backend.nerko.service.impl;

import static com.sms.eagle.eye.backend.common.Constant.PROJECT_CACHE_KEY;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_OAUTH_RESOURCE_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.nerko.client.CoordinationClient;
import com.sms.eagle.eye.backend.nerko.dto.NerkoProjectInfo;
import com.sms.eagle.eye.backend.nerko.service.NerkoCoordinationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NerkoCoordinationServiceImpl implements NerkoCoordinationService {

    private final CoordinationClient coordinationClient;

    public NerkoCoordinationServiceImpl(CoordinationClient coordinationClient) {
        this.coordinationClient = coordinationClient;
    }

    @Cacheable(value = PROJECT_CACHE_KEY)
    @Override
    public List<NerkoProjectInfo> getProjectList() {
        try {
            log.info("Get Project List");
            return coordinationClient.getProjectList().getData();
        } catch (Exception exception) {
            throw new EagleEyeException(GET_OAUTH_RESOURCE_ERROR, exception.getMessage());
        }
    }
}