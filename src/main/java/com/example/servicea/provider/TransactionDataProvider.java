package com.example.servicea.provider;

import com.example.servicea.mapper.OfTransactionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易数据服务 Dubbo 提供者
 * 
 * 封装 OfTransactionMapper 的调用，暴露为 Dubbo 服务
 * 用于测试 Mapper 层变更的跨项目影响识别
 * 
 * 测试场景：
 * 1. Mapper 方法新增 - setNull4SingleCeConfirm, setNull4DoubleCeConfirm
 * 2. 高风险变更识别
 * 3. 跨项目影响
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 10000, retries = 2)
@Component
public class TransactionDataProvider {

    @Autowired
    private OfTransactionMapper transactionMapper;

    /**
     * 根据ID查询交易记录
     * 
     * @param id 交易ID
     * @return 交易记录
     */
    public Map<String, Object> getTransactionById(Long id) {
        return transactionMapper.selectById(id);
    }

    /**
     * 更新交易状态
     * 
     * @param id 交易ID
     * @param status 新状态
     * @return 是否成功
     */
    public boolean updateTransactionStatus(Long id, String status) {
        int rows = transactionMapper.updateStatus(id, status);
        return rows > 0;
    }

    /**
     * 【新增方法】重置单次确真字段
     * 调用 OfTransactionMapper.setNull4SingleCeConfirm
     * 
     * 用于重置单次确真相关字段，以便重新进行确真流程
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public Map<String, Object> resetSingleCeConfirm(Long transactionId) {
        Map<String, Object> result = new HashMap<>();
        
        // 调用 Mapper 新增的方法
        int rows = transactionMapper.setNull4SingleCeConfirm(transactionId);
        
        result.put("success", rows > 0);
        result.put("transactionId", transactionId);
        result.put("affectedRows", rows);
        result.put("message", rows > 0 ? "单次确真字段已重置" : "重置失败");
        
        return result;
    }

    /**
     * 【新增方法】重置双确真字段
     * 调用 OfTransactionMapper.setNull4DoubleCeConfirm
     * 
     * 用于重置双确真相关字段，以便重新进行双确真流程
     * 
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public Map<String, Object> resetDoubleCeConfirm(Long transactionId) {
        Map<String, Object> result = new HashMap<>();
        
        // 调用 Mapper 新增的方法
        int rows = transactionMapper.setNull4DoubleCeConfirm(transactionId);
        
        result.put("success", rows > 0);
        result.put("transactionId", transactionId);
        result.put("affectedRows", rows);
        result.put("message", rows > 0 ? "双确真字段已重置" : "重置失败");
        
        return result;
    }

    /**
     * 根据用户ID和状态查询交易列表
     * 
     * @param userId 用户ID
     * @param status 状态
     * @return 交易列表
     */
    public List<Map<String, Object>> getTransactionsByUserAndStatus(Long userId, String status) {
        return transactionMapper.selectByUserIdAndStatus(userId, status);
    }

    /**
     * 【新增方法】批量更新确真状态
     * 
     * @param transactionIds 交易ID列表
     * @param confirmStatus 确真状态
     * @return 操作结果
     */
    public Map<String, Object> batchUpdateConfirmStatus(List<Long> transactionIds, String confirmStatus) {
        Map<String, Object> result = new HashMap<>();
        
        int rows = transactionMapper.batchUpdateConfirmStatus(transactionIds, confirmStatus);
        
        result.put("success", rows > 0);
        result.put("affectedRows", rows);
        result.put("confirmStatus", confirmStatus);
        result.put("message", String.format("成功更新 %d 条记录", rows));
        
        return result;
    }

    /**
     * 【新增方法】查询待确真的交易列表
     * 
     * @param companyId 公司ID
     * @param confirmType 确真类型（SINGLE/DOUBLE）
     * @return 待确真交易列表
     */
    public List<Map<String, Object>> getPendingConfirmTransactions(Long companyId, String confirmType) {
        return transactionMapper.selectPendingConfirmList(companyId, confirmType);
    }
}
