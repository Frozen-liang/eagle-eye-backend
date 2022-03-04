package com.sms.eagle.eye.backend.wecom.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageResponse extends AbstractBaseResponse {

    @JsonProperty("invaliduser")
    private String inValidUser;

    @JsonProperty("invalidparty")
    private String inValidParty;

    @JsonProperty("invalidtag")
    private String inValidTag;

}
