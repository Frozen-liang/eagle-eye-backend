package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.AlertEntity;
import com.sms.eagle.eye.backend.request.alert.AlertQueryRequest;
import com.sms.eagle.eye.backend.response.alert.AlertResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlertMapper extends BaseMapper<AlertEntity> {

    IPage<AlertResponse> getPage(Page<?> page, @Param("request") AlertQueryRequest request);

    List<AlertResponse> getResponseList(@Param("from") String from,
        @Param("to") String to, @Param("format") String format);
}




