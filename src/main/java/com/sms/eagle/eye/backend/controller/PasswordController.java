package com.sms.eagle.eye.backend.controller;

import com.sms.eagle.eye.backend.model.CustomPage;
import com.sms.eagle.eye.backend.model.Response;
import com.sms.eagle.eye.backend.request.password.PasswordRequest;
import com.sms.eagle.eye.backend.response.password.PasswordResponse;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密钥库.
 */
@RestController
@RequestMapping("/v1/password")
public class PasswordController {

    /**
     * 分页获取.
     */
    @GetMapping("/page")
    public Response<CustomPage<PasswordResponse>> page() {
        return null;
    }

    /**
     * 获取列表.
     */
    @GetMapping("/list")
    public Response<List<PasswordResponse>> list() {
        return null;
    }

    /**
     * 添加.
     */
    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody PasswordRequest request) {
        return null;
    }

    /**
     * 修改.
     */
    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody PasswordRequest request) {
        return null;
    }

    /**
     * 删除.
     */
    @DeleteMapping("/{id}")
    public Response<Boolean> delete(@PathVariable Long id) {
        return null;
    }
}