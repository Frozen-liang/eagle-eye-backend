package com.sms.eagle.eye.backend.wecom.exception;

import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;

public class WeComException extends RuntimeException {

    private WeComErrorCode errorCode;

    public WeComException(String message) {
        super(message);
    }

    public WeComException(String message, WeComErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public WeComErrorCode getErrorCode() {
        return errorCode;
    }
}
