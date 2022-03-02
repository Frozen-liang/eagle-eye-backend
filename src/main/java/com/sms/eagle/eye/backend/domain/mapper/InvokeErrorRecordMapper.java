package com.sms.eagle.eye.backend.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sms.eagle.eye.backend.domain.entity.InvokeErrorRecordEntity;
import com.sms.eagle.eye.backend.response.task.InvokeErrorRecordResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InvokeErrorRecordMapper extends BaseMapper<InvokeErrorRecordEntity> {

    List<InvokeErrorRecordResponse> getErrorRecords(@Param("taskId") Long taskId);
}




