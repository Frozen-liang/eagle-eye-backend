package com.sms.eagle.eye.backend.request.alert;

import static com.sms.eagle.eye.backend.common.TimePatternConstant.DATE_TIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebHookRequest {

    @NotNull
    private String uniqueValue;
    private String uniqueField;
    private String alarmMessage;
    @Builder.Default
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime alertTime = LocalDateTime.now(ZoneOffset.UTC);
}