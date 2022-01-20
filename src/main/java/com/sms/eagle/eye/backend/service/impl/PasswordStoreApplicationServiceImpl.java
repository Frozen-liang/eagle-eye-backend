package com.sms.eagle.eye.backend.service.impl;

import static com.sms.eagle.eye.backend.exception.ErrorCode.PASSWORD_KEY_HAS_ALREADY_EXIST;

import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import com.sms.eagle.eye.backend.service.PasswordStoreApplicationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PasswordStoreApplicationServiceImpl implements PasswordStoreApplicationService {

    private final PasswordStoreService passwordStoreService;

    public PasswordStoreApplicationServiceImpl(
        PasswordStoreService passwordStoreService) {
        this.passwordStoreService = passwordStoreService;
    }

    @Override
    public CustomPage<PasswordPageResponse> page(PasswordQueryRequest request) {
        return new CustomPage<>(passwordStoreService.getPage(request));
    }

    @Override
    public boolean addPassword(PasswordRequest request) {
        if (passwordStoreService.countByKey(request.getKey()) != 0) {
            throw new EagleEyeException(PASSWORD_KEY_HAS_ALREADY_EXIST);
        }
        passwordStoreService.saveFromRequest(request);
        return true;
    }

    @Override
    public boolean updatePassword(PasswordRequest request) {
        if (passwordStoreService.countByKey(request.getKey()) > 1) {
            throw new EagleEyeException(PASSWORD_KEY_HAS_ALREADY_EXIST);
        }
        passwordStoreService.updateFromRequest(request);
        // TODO 密码更新后 是否通知插件更改其配置
        return true;
    }

    @Override
    public boolean deletePassword(Long id) {
        passwordStoreService.deletePasswordById(id);
        return true;
    }

    @Override
    public List<PasswordSelectResponse> getList() {
        return passwordStoreService.getList();
    }
}