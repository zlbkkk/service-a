package com.example.servicea.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 项目数据 Mapper
 * 提供项目相关的数据库操作
 */
@Mapper
public interface OfProjectMapper {

    /**
     * 分页查询项目列表
     * @param params 查询参数
     * @return 项目列表
     */
    List<Map<String, Object>> listPage(@Param("params") Map<String, Object> params);

    /**
     * 新增项目
     * @param project 项目数据
     * @return 影响行数
     */
    int addProject(@Param("project") Map<String, Object> project);

    /**
     * 更新项目
     * @param project 项目数据
     * @return 影响行数
     */
    int updateProject(@Param("project") Map<String, Object> project);

    /**
     * 根据买方查询项目列表
     * @param buyerId 买方ID
     * @return 项目列表
     */
    List<Map<String, Object>> listProjectByBuyer(@Param("buyerId") String buyerId);

    /**
     * 获取在线项目信息
     * @param projectId 项目ID
     * @return 项目信息
     */
    Map<String, Object> getOnlineProject(@Param("projectId") String projectId);

    /**
     * 同步贷后材料
     * @param projectId 项目ID
     * @return 同步结果
     */
    int syncPostLoan(@Param("projectId") String projectId);
}
