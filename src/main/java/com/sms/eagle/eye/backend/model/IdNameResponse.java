package com.sms.eagle.eye.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class IdNameResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private T id;
    private String name;
}