package com.sms.eagle.eye.backend.response.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class TaskGroupBasicResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String name;
    private Integer index;
}