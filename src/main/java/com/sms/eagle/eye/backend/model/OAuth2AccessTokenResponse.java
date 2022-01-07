package com.sms.eagle.eye.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AccessTokenResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;
    @JsonProperty(value = "id_token")
    private String idToken;
    @JsonProperty(value = "refresh_token")
    private String refreshToken;
}