package com.sms.eagle.eye.backend.wecom.client.decoder;


import com.sms.eagle.eye.backend.wecom.enums.WeComErrorCode;
import com.sms.eagle.eye.backend.wecom.exception.WeComException;
import com.sms.eagle.eye.backend.wecom.response.AbstractBaseResponse;
import feign.Response;
import feign.jackson.JacksonDecoder;
import java.io.IOException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomJacksonDecoder extends JacksonDecoder {

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Object result = super.decode(response, type);
        if (result instanceof AbstractBaseResponse) {
            AbstractBaseResponse abstractBaseResponse = (AbstractBaseResponse) result;
            if (abstractBaseResponse.getErrCode().equals(WeComErrorCode.ERROR_CODE_0.getErrorCode())) {
                return result;
            }
            throw new WeComException(abstractBaseResponse.getErrMsg(),
                WeComErrorCode.errorCode(abstractBaseResponse.getErrCode()));
        }
        return result;
    }
}
