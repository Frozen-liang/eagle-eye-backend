package com.sms.eagle.eye.backend.nerko.client.decoder;

import static com.sms.eagle.eye.backend.nerko.enums.NerkoErrorCode.SUCCESS;

import com.sms.eagle.eye.backend.nerko.enums.NerkoErrorCode;
import com.sms.eagle.eye.backend.nerko.exception.NerkoException;
import com.sms.eagle.eye.backend.nerko.response.NerkoBaseResponse;
import feign.Response;
import feign.jackson.JacksonDecoder;
import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpStatus;

public class NerkoJacksonDecoder extends JacksonDecoder {

    @Override
    public Object decode(Response response, Type type) throws IOException {
        HttpStatus status = HttpStatus.valueOf(response.status());
        Object result = super.decode(response, type);
        if (result instanceof NerkoBaseResponse) {
            NerkoBaseResponse nerkoBaseResponse = (NerkoBaseResponse) result;
            if (nerkoBaseResponse.getCode().equals(SUCCESS.getCode())) {
                return result;
            }
            throw new NerkoException(nerkoBaseResponse.getMessage(),
                NerkoErrorCode.resolve(nerkoBaseResponse.getCode()));
        }
        return result;
    }
}