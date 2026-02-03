package com.example.servicea.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 项目数据 Mapper
 */
@Mapper
public interface OfProjectMapper {

    /**
     * 分页查询项目列表
     */
    List<Map<String, Object>> listPage(@Param("params") Map<String, Object> params);
}
