package com.school.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.school.management.entity.OperationLog;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 操作日志Mapper
 */
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    @Select("SELECT * FROM operation_log ORDER BY created_at DESC LIMIT 50")
    java.util.List<Map<String, Object>> selectRecentLogs();
}
