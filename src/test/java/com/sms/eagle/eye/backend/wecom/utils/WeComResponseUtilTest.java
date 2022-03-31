package com.sms.eagle.eye.backend.wecom.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;
import com.sms.eagle.eye.backend.wecom.exception.WeComException;
import com.sms.eagle.eye.backend.wecom.response.AbstractBaseResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class WeComResponseUtilTest {


    private static final MockedStatic<WeComErrorCode> WE_COM_ERROR_CODE_MOCKED_STATIC
        = mockStatic(WeComErrorCode.class);

    @AfterAll
    public static void close() {
        WE_COM_ERROR_CODE_MOCKED_STATIC.close();
    }


    /**
     * {@link WeComResponseUtil#isSuccess(AbstractBaseResponse)}
     *
     * <p>情形1：AbstractBaseResponse 为空
     */
    @Test
    void isSuccess_test_1() {
        // request
        AbstractBaseResponse baseResponse = null;
        // invoke
        boolean result = WeComResponseUtil.isSuccess(baseResponse);
        // assert
        assertThat(result).isFalse();
    }

    /**
     * {@link WeComResponseUtil#isSuccess(AbstractBaseResponse)}
     *
     * <p>情形2：AbstractBaseResponse 不为空，
     * 且 errorCode 等于 {@link WeComErrorCode#ERROR_CODE_0}
     */
    @Test
    void isSuccess_test_2() {
        // request
        AbstractBaseResponse baseResponse = mock(AbstractBaseResponse.class);
        // mock baseResponse.getErrCode()
        String errorCode = WeComErrorCode.ERROR_CODE_0.getErrorCode();
        when(baseResponse.getErrCode()).thenReturn(errorCode);
        // invoke
        boolean result = WeComResponseUtil.isSuccess(baseResponse);
        // assert
        assertThat(result).isTrue();
    }

    /**
     * {@link WeComResponseUtil#isSuccess(AbstractBaseResponse)}
     *
     * <p>情形2：AbstractBaseResponse 不为空，
     * 且 errorCode 不等于 {@link WeComErrorCode#ERROR_CODE_0}
     */
    @Test
    void isSuccess_test_3() {
        // request
        AbstractBaseResponse baseResponse = mock(AbstractBaseResponse.class);
        // mock baseResponse.getErrCode()
        String errorCode = "errorCode";
        when(baseResponse.getErrCode()).thenReturn(errorCode);
        // mock static
        WeComErrorCode weComErrorCode = mock(WeComErrorCode.class);
        WE_COM_ERROR_CODE_MOCKED_STATIC.when(() -> WeComErrorCode.errorCode(errorCode))
            .thenReturn(weComErrorCode);
        // invoke and assert
        assertThatThrownBy(() -> WeComResponseUtil.isSuccess(baseResponse))
            .isInstanceOf(WeComException.class);
    }
}