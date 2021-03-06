package com.sms.eagle.eye.backend.controller;

import static com.sms.eagle.eye.backend.common.enums.PermissionType.PLUGIN_ADD;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PLUGIN_DELETE;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PLUGIN_EDIT;
import static com.sms.eagle.eye.backend.common.enums.PermissionType.PLUGIN_ENABLE_DISABLE;

import com.sms.eagle.eye.backend.common.annotation.PreAuth;
import com.sms.eagle.eye.backend.common.enums.PermissionType;
import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.request.plugin.PluginQueryRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginRequest;
import com.sms.eagle.eye.backend.request.plugin.PluginUpdateRequest;
import com.sms.eagle.eye.backend.response.Response;
import com.sms.eagle.eye.backend.response.plugin.PluginDetailResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginMetadataResponse;
import com.sms.eagle.eye.backend.response.plugin.PluginResponse;
import com.sms.eagle.eye.backend.service.PluginApplicationService;
import com.sms.eagle.eye.backend.service.PluginRpcService;
import java.util.List;
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
 * 插件模块.
 */
@RestController
@RequestMapping("/v1/plugin")
public class PluginController {

    private final PluginRpcService pluginRpcService;
    private final PluginApplicationService pluginApplicationService;

    public PluginController(PluginRpcService pluginRpcService,
        PluginApplicationService pluginApplicationService) {
        this.pluginRpcService = pluginRpcService;
        this.pluginApplicationService = pluginApplicationService;
    }

    /**
     * 获取插件元数据.
     */
    @GetMapping("/metadata")
    public Response<PluginMetadataResponse> getPluginMetadata(@RequestParam String url) {
        return Response.ok(pluginRpcService.getMetadataResponseByTarget(url));
    }

    /**
     * 根据插件id获取其详情.
     */
    @GetMapping("/{id}")
    public Response<PluginDetailResponse> getPluginDetail(@PathVariable Long id) {
        return Response.ok(pluginApplicationService.getPluginDetailById(id));
    }

    /**
     * 分页查询插件列表.
     */
    @GetMapping("/page")
    public Response<CustomPage<PluginResponse>> page(PluginQueryRequest request) {
        return Response.ok(pluginApplicationService.page(request));
    }

    /**
     * 添加插件.
     */
    @PostMapping
    @PreAuth(PLUGIN_ADD)
    public Response<Boolean> add(@Validated @RequestBody PluginRequest request) {
        return Response.ok(pluginApplicationService.addPlugin(request));
    }

    /**
     * 编辑插件.
     */
    @PutMapping
    @PreAuth(PLUGIN_EDIT)
    public Response<Boolean> update(@Validated @RequestBody PluginUpdateRequest request) {
        return Response.ok(pluginApplicationService.updatePlugin(request));
    }

    /**
     * 根据id删除插件.
     */
    @DeleteMapping("/{id}")
    @PreAuth(PLUGIN_DELETE)
    public Response<Boolean> remove(@PathVariable Long id) {
        return Response.ok(pluginApplicationService.deletePlugin(id));
    }

    /**
     * 启用插件.
     */
    @PutMapping("/enable/{id}")
    @PreAuth(PLUGIN_ENABLE_DISABLE)
    public Response<Boolean> enable(@PathVariable Long id) {
        return Response.ok(pluginApplicationService.enablePlugin(id));
    }

    /**
     * 禁用插件.
     */
    @PutMapping("/disable/{id}")
    @PreAuth(PLUGIN_ENABLE_DISABLE)
    public Response<Boolean> disable(@PathVariable Long id) {
        return Response.ok(pluginApplicationService.disablePlugin(id));
    }


    @GetMapping("/alarm-level/all")
    public Response<List<Integer>> getAllAlarmLevel(@RequestParam Long pluginId) {
        return Response.ok(pluginApplicationService.getAllAlarmLevel(pluginId));
    }
}