package com.sms.eagle.eye.backend.response.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordSelectResponse {

    private String key;
    private String description;
    private String creator;

}