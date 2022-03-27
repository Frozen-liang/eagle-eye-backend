package com.sms.eagle.eye.backend.nerko.service.impl;

import static com.sms.eagle.eye.backend.common.Constant.USER_CACHE_KEY;
import static com.sms.eagle.eye.backend.exception.ErrorCode.GET_OAUTH_RESOURCE_ERROR;

import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.nerko.client.UserManagementClient;
import com.sms.eagle.eye.backend.nerko.dto.NerkoUserInfo;
import com.sms.eagle.eye.backend.nerko.service.NerkoUserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NerkoUserServiceImpl implements NerkoUserService {

    private final UserManagementClient userManagementClient;

    public NerkoUserServiceImpl(UserManagementClient userManagementClient) {
        this.userManagementClient = userManagementClient;
    }

    @Cacheable(value = USER_CACHE_KEY)
    @Override
    public List<NerkoUserInfo> getUserList() {
        try {
            log.info("Get User List");
            return userManagementClient.getUserList().getData();
        } catch (Exception exception) {
            throw new EagleEyeException(GET_OAUTH_RESOURCE_ERROR, exception.getMessage());
        }
    }
}