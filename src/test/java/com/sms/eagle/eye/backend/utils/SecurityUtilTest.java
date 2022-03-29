package com.sms.eagle.eye.backend.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.context.UserContextHolder;
import com.sms.eagle.eye.backend.exception.UnauthorizedException;
import com.sms.eagle.eye.backend.model.UserInfo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class SecurityUtilTest {

    private static final MockedStatic<UserContextHolder> USER_CONTEXT_HOLDER_MOCKED_STATIC
        = mockStatic(UserContextHolder.class);

    @AfterAll
    public static void close() {
        USER_CONTEXT_HOLDER_MOCKED_STATIC.close();
    }

    /**
     * {@link SecurityUtil#getCurrentUser()}
     *
     * <p>情形1：成功获取当前用户信息
     */
    @Test
    void getCurrentUser_test_1() {
        UserInfo userInfo = mock(UserInfo.class);
        String username = "username";
        when(userInfo.getUsername()).thenReturn(username);
        // mock static UserContextHolder.getUserInfo()
        USER_CONTEXT_HOLDER_MOCKED_STATIC.when(UserContextHolder::getUserInfo).thenReturn(userInfo);
        // invoke
        UserInfo result = SecurityUtil.getCurrentUser();
        // assert
        assertThat(result).isEqualTo(userInfo);
    }

    /**
     * {@link SecurityUtil#getCurrentUser()}
     *
     * <p>情形2：从{@link UserContextHolder#getUserInfo()}获取到的用户信息为空
     */
    @Test
    void getCurrentUser_test_3() {
        UserInfo userInfo = mock(UserInfo.class);
        when(userInfo.getUsername()).thenReturn(null);
        // mock static UserContextHolder.getUserInfo()
        USER_CONTEXT_HOLDER_MOCKED_STATIC.when(UserContextHolder::getUserInfo).thenReturn(userInfo);
        // invoke and verify
        assertThatThrownBy(SecurityUtil::getCurrentUser).isInstanceOf(UnauthorizedException.class);
    }

    /**
     * {@link SecurityUtil#getCurrentUser()}
     *
     * <p>情形2：从{@link UserContextHolder#getUserInfo()}获取到的用户信息中用户名为空
     */
    @Test
    void getCurrentUser_test_2() {
        // mock static UserContextHolder.getUserInfo()
        USER_CONTEXT_HOLDER_MOCKED_STATIC.when(UserContextHolder::getUserInfo).thenReturn(null);
        // invoke and verify
        assertThatThrownBy(SecurityUtil::getCurrentUser).isInstanceOf(UnauthorizedException.class);
    }
}