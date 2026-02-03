package com.example.servicea.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Mapper
 */
@Mapper
public interface OfTransactionMapper2 {

    /**
     * 分页查询融资交易列表
     */
    List<Map<String, Object>> page(@Param("params") Map<String, Object> params);
}
