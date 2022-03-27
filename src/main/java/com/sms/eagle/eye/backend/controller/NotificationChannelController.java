package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.common.enums.NotificationChannelType;
import com.sms.eagle.eye.backend.common.validator.InsertGroup;
import com.sms.eagle.eye.backend.common.validator.UpdateGroup;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelQueryRequest;
import com.sms.eagle.eye.backend.request.channel.NotificationChannelRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.channel.ChannelDetailResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelFieldResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelListResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelPageResponse;
import com.sms.eagle.eye.backend.response.channel.ChannelTypeResponse;
import com.sms.eagle.eye.backend.service.NotificationChannelApplicationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息通道 模块.
 */
@Slf4j
@RestController
@RequestMapping("/v1/notification/channel")
public class NotificationChannelController {

    private final NotificationChannelApplicationService notificationChannelApplicationService;

    public NotificationChannelController(
        NotificationChannelApplicationService notificationChannelApplicationService) {
        this.notificationChannelApplicationService = notificationChannelApplicationService;
    }

    /**
     * 获取通道实例列表.
     */
    @GetMapping("/list")
    public Response<List<ChannelListResponse>> getList() {
        return Response.ok(notificationChannelApplicationService.getList());
    }

    /**
     * 分页获取通道实例列表.
     */
    @GetMapping("/page")
    public Response<CustomPage<ChannelPageResponse>> getPage(NotificationChannelQueryRequest request) {
        return Response.ok(notificationChannelApplicationService.getPage(request));
    }

    /**
     * 获取通道类型.
     */
    @GetMapping("/type")
    public Response<List<ChannelTypeResponse>> getTypes() {
        return Response.ok(NotificationChannelType.getList());
    }

    /**
     * 根据通道类型获取配置表单.
     */
    @GetMapping("/config-fields")
    public Response<List<ChannelFieldResponse>> getConfigFieldsByType(@RequestParam Integer type) {
        return Response.ok(notificationChannelApplicationService.getConfigFieldsByType(type));
    }

    /**
     * 根据通道类型获取输入数据表单.
     */
    @GetMapping("/input-fields")
    public Response<List<ChannelFieldResponse>> getInputFieldsByType(@RequestParam Integer type) {
        return Response.ok(notificationChannelApplicationService.getInputFieldsByType(type));
    }

    /**
     * 根据 channelId 获取通道实例的配置详情.
     */
    @GetMapping("/{channelId}")
    public Response<ChannelDetailResponse> getByChannelId(@PathVariable Long channelId) {
        return Response.ok(notificationChannelApplicationService.getByChannelId(channelId));
    }

    /**
     * 添加通道实例.
     */
    @PostMapping
    public Response<Boolean> add(
        @Validated(value = InsertGroup.class) @RequestBody NotificationChannelRequest request) {
        return Response.ok(notificationChannelApplicationService.addChannel(request));
    }

    /**
     * 编辑通道实例.
     */
    @PutMapping
    public Response<Boolean> edit(
        @Validated(value = UpdateGroup.class) @RequestBody NotificationChannelRequest request) {
        return Response.ok(notificationChannelApplicationService.updateChannel(request));
    }

    /**
     * 根据 channelId 删除通道实例.
     */
    @DeleteMapping("/{channelId}")
    public Response<Boolean> delete(@PathVariable Long channelId) {
        return Response.ok(notificationChannelApplicationService.removeChannel(channelId));
    }
}