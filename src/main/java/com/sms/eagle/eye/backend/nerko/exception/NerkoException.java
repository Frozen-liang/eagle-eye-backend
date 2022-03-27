package com.sms.eagle.eye.backend.nerko.exception;

import com.sms.eagle.eye.backend.nerko.enums.NerkoErrorCode;

public class NerkoException extends RuntimeException {

    private NerkoErrorCode errorCode;

    public NerkoException(String message, NerkoErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public NerkoErrorCode getErrorCode() {
        return errorCode;
    }
}