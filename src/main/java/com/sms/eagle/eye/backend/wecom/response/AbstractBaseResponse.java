package com.sms.eagle.eye.backend.wecom.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class AbstractBaseResponse {

    @JsonProperty("errcode")
    private String errCode;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("next_cursor")
    private String nextCursor;

}
