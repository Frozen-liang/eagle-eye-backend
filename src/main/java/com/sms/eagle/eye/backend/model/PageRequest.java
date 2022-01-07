package com.sms.eagle.eye.backend.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest<T> {

    /**
     * 当前页.
     */
    private Long current = 1L;
    /**
     * 每页显示条数.
     */
    private Long size = 10L;

    public Page<T> getPageInfo() {
        return new Page<>(current, size);
    }
}