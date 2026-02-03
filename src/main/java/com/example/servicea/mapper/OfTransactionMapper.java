package com.example.servicea.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易记录 Mapper 接口
 * 
 * 模拟 OfTransactionMapper 的变更类型，用于测试分析系统
 * 测试场景：
 * 1. Mapper 方法新增 - setNull4SingleCeConfirm, setNull4DoubleCeConfirm
 * 2. 高风险变更（涉及数据更新）
 * 3. 跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@Mapper
public interface OfTransactionMapper {

    /**
     * 根据ID查询交易记录
     * 
     * @param id 交易ID
     * @return 交易记录
     */
    Map<String, Object> selectById(@Param("id") Long id);

    /**
     * 根据订单ID查询交易记录
     * 
     * @param orderId 订单ID
     * @return 交易记录列表
     */
    List<Map<String, Object>> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 更新交易状态
     * 
     * @param id 交易ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 【新增方法】将单次确真字段置空
     * 模拟 OfTransactionMapper.setNull4SingleCeConfirm
     * 
     * 用于重置单次确真相关字段，以便重新进行确真流程
     * 
     * @param transactionId 交易ID
     * @return 影响行数
     */
    int setNull4SingleCeConfirm(@Param("transactionId") Long transactionId);

    /**
     * 【新增方法】将双确真字段置空
     * 模拟 OfTransactionMapper.setNull4DoubleCeConfirm
     * 
     * 用于重置双确真相关字段，以便重新进行双确真流程
     * 
     * @param transactionId 交易ID
     * @return 影响行数
     */
    int setNull4DoubleCeConfirm(@Param("transactionId") Long transactionId);

    /**
     * 根据用户ID和状态查询交易列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 交易列表
     */
    List<Map<String, Object>> selectByUserIdAndStatus(
            @Param("userId") Long userId, 
            @Param("status") String status);

    /**
     * 【新增方法】批量更新确真状态
     * 
     * @param transactionIds 交易ID列表
     * @param confirmStatus 确真状态
     * @return 影响行数
     */
    int batchUpdateConfirmStatus(
            @Param("transactionIds") List<Long> transactionIds,
            @Param("confirmStatus") String confirmStatus);

    /**
     * 【新增方法】查询待确真的交易列表
     * 
     * @param companyId 公司ID
     * @param confirmType 确真类型（SINGLE/DOUBLE）
     * @return 待确真交易列表
     */
    List<Map<String, Object>> selectPendingConfirmList(
            @Param("companyId") Long companyId,
            @Param("confirmType") String confirmType);

    // ========== 以下方法对应前端 API 调用 ==========

    /**
     * 分页查询交易列表
     * 对应前端: /ofTransaction/page
     * 
     * @param params 查询参数
     * @return 交易列表分页数据
     */
    List<Map<String, Object>> page(@Param("params") Map<String, Object> params);

    /**
     * 获取融资汇总信息
     * 对应前端: /ofTransaction/getTransactionSumInfo
     * 
     * @return 融资汇总信息
     */
    Map<String, Object> getTransactionSumInfo();

    /**
     * 获取交易详情
     * 对应前端: /ofTransaction/getTransactionDetail
     * 
     * @param transactionNo 交易编号
     * @return 交易详情
     */
    Map<String, Object> getTransactionDetail(@Param("transactionNo") String transactionNo);

    /**
     * 获取供应商上一次回款账户信息
     * 对应前端: /ofTransaction/getSpyLastReturnAccount
     * 
     * @return 回款账户信息
     */
    Map<String, Object> getSpyLastReturnAccount();

    /**
     * 更新融资信息
     * 对应前端: /ofTransaction/updateTransactionInfo
     * 
     * @param transactionInfo 融资信息
     * @return 影响行数
     */
    int updateTransactionInfo(@Param("info") Map<String, Object> transactionInfo);

    /**
     * 批量删除交易
     * 对应前端: /ofTransaction/batchDel
     * 
     * @param transactionNos 交易编号列表
     * @return 影响行数
     */
    int batchDel(@Param("transactionNos") List<String> transactionNos);
}
