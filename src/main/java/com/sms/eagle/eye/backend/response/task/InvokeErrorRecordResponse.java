package com.sms.eagle.eye.backend.response.task;

import static com.sms.eagle.eye.backend.common.TimePatternConstant.DATE_TIME_PATTERN;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvokeErrorRecordResponse {

    private String errMessage;
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime createTime;
}