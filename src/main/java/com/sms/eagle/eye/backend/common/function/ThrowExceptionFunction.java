package com.sms.eagle.eye.backend.common.function;

import com.sms.eagle.eye.backend.exception.ErrorCode;

@FunctionalInterface
public interface ThrowExceptionFunction {

    void throwMessage(ErrorCode errorCode);

}
