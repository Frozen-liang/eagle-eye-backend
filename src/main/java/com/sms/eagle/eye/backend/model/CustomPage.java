package com.sms.eagle.eye.backend.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomPage<T> {

    /**
     * 查询数据列表.
     */
    private List<T> records;
    /**
     * 总数.
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10.
     */
    private long size = 10;
    /**
     * 当前页.
     */
    private long current = 1;
    /**
     * 当前分页总页数.
     */
    private long pages;

    public CustomPage(IPage<T> page) {
        this.records = page.getRecords();
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.pages = page.getPages();
        this.total = page.getTotal();
    }

}