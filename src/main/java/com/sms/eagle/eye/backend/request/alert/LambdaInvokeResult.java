package com.sms.eagle.eye.backend.request.alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LambdaInvokeResult {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskId;
    private String mappingId;
    @NotNull
    private Boolean success;
    private String errorMsg;
}