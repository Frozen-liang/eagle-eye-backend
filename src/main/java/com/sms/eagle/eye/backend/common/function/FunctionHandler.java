package com.sms.eagle.eye.backend.common.function;

import com.sms.eagle.eye.backend.exception.EagleEyeException;

public interface FunctionHandler {

    static ThrowExceptionFunction isTrue(boolean isConfirmed) {
        return (errorCode) -> {
            if (isConfirmed) {
                throw new EagleEyeException(errorCode);
            }
        };
    }

}
