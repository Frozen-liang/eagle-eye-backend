package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.exception.ErrorCode;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import com.sms.eagle.eye.backend.response.password.PasswordSelectResponse;
import com.sms.eagle.eye.backend.service.impl.PasswordStoreApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class PasswordStoreApplicationServiceTest {

    private final PasswordStoreService passwordStoreService = mock(PasswordStoreService.class);
    private final PasswordStoreApplicationService passwordStoreApplicationService =
            new PasswordStoreApplicationServiceImpl(passwordStoreService);
    private final PasswordQueryRequest queryRequest = mock(PasswordQueryRequest.class);
    private final PasswordRequest passwordRequest = mock(PasswordRequest.class);

    private static final String STRING = null;
    private static final Long ID = 1L;

    @Test
    @DisplayName("Test the page method in the Password Store Application Service")
    public void page_test() {
        // mock
        IPage<PasswordPageResponse> iPage = mock(IPage.class);
        List<PasswordPageResponse> list = mock(List.class);
        doReturn(list).when(iPage).getRecords();
        when(passwordStoreService.getPage(queryRequest)).thenReturn(iPage);
        // 执行
        CustomPage<PasswordPageResponse> result = passwordStoreApplicationService.page(queryRequest);
        //
        assertThat(result.getRecords()).isEqualTo(list);
    }

    @Test
    @DisplayName("Test the addPassword method in the Password Store Application Service")
    public void addPassword_test() {
        // mock
        doNothing().when(passwordStoreService).saveFromRequest(passwordRequest);
        // 执行
        boolean result = passwordStoreApplicationService.addPassword(passwordRequest);
        // 验证
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the addPassword method in the Password Store Application Service")
    public void addPassword_exception_test() {
        // mock
        when(passwordRequest.getKey()).thenReturn(STRING);
        when(passwordStoreService.countByKey(STRING))
                .thenThrow(new EagleEyeException(ErrorCode.PASSWORD_KEY_HAS_ALREADY_EXIST));
        // 验证异常
        assertThatThrownBy(() -> passwordStoreApplicationService
                .addPassword(passwordRequest)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the updatePassword method in the Password Store Application Service")
    public void updatePassword_test() {
        // mock
        doNothing().when(passwordStoreService).updateFromRequest(passwordRequest);
        // 执行
        boolean result = passwordStoreApplicationService.updatePassword(passwordRequest);
        // 验证
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the updatePassword method in the Password Store Application Service")
    public void updatePassword_exception_test() {
        // mock
        when(passwordRequest.getKey()).thenReturn(STRING);
        when(passwordStoreService.countByKey(STRING))
                .thenThrow(new EagleEyeException(ErrorCode.PASSWORD_KEY_HAS_ALREADY_EXIST));
        // 验证异常
        assertThatThrownBy(() -> passwordStoreApplicationService
                .updatePassword(passwordRequest)).isInstanceOf(EagleEyeException.class);
    }


    @Test
    @DisplayName("Test the deletePassword method in the Password Store Application Service")
    public void deletePassword_test() {
        // mock
        doNothing().when(passwordStoreService).deletePasswordById(ID);
        // 执行
        boolean result = passwordStoreApplicationService.deletePassword(ID);
        // 验证
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the getList method in the Password Store Application Service")
    public void getList_test() {
        // mock
        List<PasswordSelectResponse> list = mock(List.class);
        when(passwordStoreService.getList()).thenReturn(list);
        //执行
        List<PasswordSelectResponse> result = passwordStoreApplicationService.getList();
        // 验证
        assertThat(result).isEqualTo(list);
    }
}
