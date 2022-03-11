package com.sms.eagle.eye.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.service.PasswordStoreService;
import com.sms.eagle.eye.backend.exception.EagleEyeException;
import com.sms.eagle.eye.backend.exception.ErrorCode;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.service.impl.PasswordStoreApplicationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

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
        when(passwordStoreService.getPage(queryRequest)).thenReturn(new Page<>());
        assertThat(passwordStoreApplicationService.page(queryRequest)).isNotNull();
    }

    @Test
    @DisplayName("Test the addPassword method in the Password Store Application Service")
    public void addPassword_test() {
        doNothing().when(passwordStoreService).saveFromRequest(passwordRequest);
        assertThat(passwordStoreApplicationService.addPassword(passwordRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the addPassword method in the Password Store Application Service")
    public void addPassword_exception_test() {
        when(passwordRequest.getKey()).thenReturn(STRING);
        when(passwordStoreService.countByKey(STRING))
                .thenThrow(new EagleEyeException(ErrorCode.PASSWORD_KEY_HAS_ALREADY_EXIST));
        assertThatThrownBy(() -> passwordStoreApplicationService
                .addPassword(passwordRequest)).isInstanceOf(EagleEyeException.class);
    }

    @Test
    @DisplayName("Test the updatePassword method in the Password Store Application Service")
    public void updatePassword_test() {
        doNothing().when(passwordStoreService).updateFromRequest(passwordRequest);
        assertThat(passwordStoreApplicationService.updatePassword(passwordRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the updatePassword method in the Password Store Application Service")
    public void updatePassword_exception_test() {
        when(passwordRequest.getKey()).thenReturn(STRING);
        when(passwordStoreService.countByKey(STRING))
                .thenThrow(new EagleEyeException(ErrorCode.PASSWORD_KEY_HAS_ALREADY_EXIST));
        assertThatThrownBy(() -> passwordStoreApplicationService
                .updatePassword(passwordRequest)).isInstanceOf(EagleEyeException.class);
    }


    @Test
    @DisplayName("Test the deletePassword method in the Password Store Application Service")
    public void deletePassword_test() {
        doNothing().when(passwordStoreService).deletePasswordById(ID);
        assertThat(passwordStoreApplicationService.deletePassword(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the getList method in the Password Store Application Service")
    public void getList_test() {
        when(passwordStoreService.getList()).thenReturn(Collections.emptyList());
        assertThat(passwordStoreApplicationService.getList()).isNotNull();
    }
}
