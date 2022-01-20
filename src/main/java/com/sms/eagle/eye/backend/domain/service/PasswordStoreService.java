package com.sms.eagle.eye.backend.domain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import java.util.List;

public interface PasswordStoreService extends IService<PasswordStoreEntity> {

    Integer countByKey(String key);

    IPage<PasswordPageResponse> getPage(PasswordQueryRequest request);

    void saveFromRequest(PasswordRequest request);

    void updateFromRequest(PasswordRequest request);

    void deletePasswordById(Long id);

    List<PasswordSelectResponse> getList();

    String getValueByKey(String key);
}
