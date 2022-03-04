package com.sms.eagle.eye.backend.wecom.service;

import com.sms.eagle.eye.backend.wecom.client.WeComClient;
import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;
import com.sms.eagle.eye.backend.wecom.exception.WeComException;
import com.sms.eagle.eye.backend.wecom.response.AbstractBaseResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractBaseService {

    @Autowired
    protected WeComClient weComClient;

    final boolean isSuccess(AbstractBaseResponse baseResponse) {
        if (Objects.nonNull(baseResponse)) {
            if (WeComErrorCode.ERROR_CODE_0.getErrorCode().equals(baseResponse.getErrCode())) {
                return true;
            } else {
                WeComErrorCode errorCode = WeComErrorCode.errorCode(baseResponse.getErrCode());
                log.error("Wecom err：Code[{}],msg:[{}],response:{}", errorCode.getErrorCode(),
                    errorCode.getErrorDesc(), baseResponse.getErrMsg());
                throw new WeComException(errorCode.getErrorDesc(), errorCode);
            }
        } else {
            return false;
        }

    }

}
