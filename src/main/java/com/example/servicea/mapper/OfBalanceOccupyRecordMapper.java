package com.example.servicea.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 余额占用记录 Mapper 接口
 * 
 * 模拟 OfBalanceOccupyRecordMapper 的变更类型，用于测试分析系统
 * 测试场景：
 * 1. Mapper 方法新增
 * 2. 分页查询方法
 * 3. 跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@Mapper
public interface OfBalanceOccupyRecordMapper {

    /**
     * 根据ID查询占用记录
     * 
     * @param id 记录ID
     * @return 占用记录
     */
    Map<String, Object> selectById(@Param("id") Long id);

    /**
     * 根据订单ID查询占用记录列表
     * 
     * @param orderId 订单ID
     * @return 占用记录列表
     */
    List<Map<String, Object>> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 【新增方法】分页查询占用明细
     * 模拟 OfBalanceOccupyRecordMapper.occupationDetailPage
     * 
     * @param userId 用户ID
     * @param status 状态
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    List<Map<String, Object>> occupationDetailPage(
            @Param("userId") Long userId,
            @Param("status") String status,
            @Param("pageNum") int pageNum,
            @Param("pageSize") int pageSize);

    /**
     * 统计占用记录总数
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 总数
     */
    int countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 【新增方法】批量查询占用详情
     * 
     * @param ids 记录ID列表
     * @return 占用详情列表
     */
    List<Map<String, Object>> selectBatchByIds(@Param("ids") List<Long> ids);
}
