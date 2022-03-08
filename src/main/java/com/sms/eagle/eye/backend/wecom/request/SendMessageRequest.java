package com.sms.eagle.eye.backend.wecom.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sms.eagle.eye.backend.wecom.dto.MsgMarkdown;
import com.sms.eagle.eye.backend.wecom.dto.MsgText;
import com.sms.eagle.eye.backend.wecom.enums.MsgType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {

    @JsonProperty("touser")
    private String toUser;

    @JsonProperty("toparty")
    private String toParty;

    @JsonProperty("totag")
    private String toTag;

    @JsonProperty("msgtype")
    private MsgType msgType;

    @JsonProperty("agentid")
    private String agentId;

    private MsgText text;

    private MsgMarkdown markdown;

    /**
     * 表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认为0.
     */
    private Integer safe;

    /**
     * 表示是否开启id转译，0表示否，1表示是，默认0.
     */
    @JsonProperty("enable_id_trans")
    private Integer enableIdTrans;

    /**
     * 表示是否开启重复消息检查，0表示否，1表示是，默认0.
     */
    @JsonProperty("enable_duplicate_check")
    private Integer enableDuplicateCheck;

    /**
     * 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时.
     */
    @JsonProperty("duplicate_check_interval")
    private Integer duplicateCheckInterval;

}
