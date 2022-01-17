package com.sms.eagle.eye.backend.response.password;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private String description;

}