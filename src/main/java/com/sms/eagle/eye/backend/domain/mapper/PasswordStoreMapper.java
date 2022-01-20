package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sms.eagle.eye.backend.domain.entity.PasswordStoreEntity;
import com.sms.eagle.eye.backend.request.password.PasswordQueryRequest;
import com.sms.eagle.eye.backend.response.password.PasswordPageResponse;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PasswordStoreMapper extends BaseMapper<PasswordStoreEntity> {

    Integer selectCountByKey(@Param("key") String key);

    IPage<PasswordPageResponse> getPage(Page<?> page, @Param("request") PasswordQueryRequest request);

    Optional<String> getValueByKey(@Param("key") String key);
}




