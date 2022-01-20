package com.sms.eagle.eye.backend.service;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import java.util.List;

public interface PasswordStoreApplicationService {

    CustomPage<PasswordPageResponse> page(PasswordQueryRequest request);

    boolean addPassword(PasswordRequest request);

    boolean updatePassword(PasswordRequest request);

    boolean deletePassword(Long id);

    List<PasswordSelectResponse> getList();
}
