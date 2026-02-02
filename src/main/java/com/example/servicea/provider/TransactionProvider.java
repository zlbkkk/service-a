package com.example.servicea.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * 交易服务 Dubbo 提供者
 * 
 * 模拟 OfTransactionService 的变动类型，用于测试分析系统
 * 测试场景：
 * 1. 方法新增 - 新增 icbcLoanApplySuccessProcess 等方法
 * 2. 关键逻辑删除 - 移除金额计算的除以10000逻辑
 * 3. 方法增强 + 重命名 - querySingleDetailResult 替代 queryDetailResult
 * 4. 查询逻辑优化 - 新增排序条件
 * 5. 业务场景新增 - 新增双确真工作流场景
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 10000, retries = 2)
@Component
public class TransactionProvider {

    /**
     * 【新增方法】工行贷款申请成功处理
     * 模拟 OfTransactionServiceImpl.icbcLoanApplySuccessProcess
     * 
     * @param transactionId 交易ID
     * @param loanAmount 贷款金额
     * @return 处理结果
     */
    public Map<String, Object> icbcLoanApplySuccessProcess(Long transactionId, BigDecimal loanAmount) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("transactionId", transactionId);
        result.put("loanAmount", loanAmount);
        result.put("status", "LOAN_APPROVED");
        result.put("message", "工行贷款申请成功");
        
        return result;
    }

    /**
     * 【新增方法】工行贷款申请拒绝处理
     * 模拟 OfTransactionServiceImpl.icbcLoanApplyRejectProcess
     * 
     * @param transactionId 交易ID
     * @param rejectReason 拒绝原因
     * @return 处理结果
     */
    public Map<String, Object> icbcLoanApplyRejectProcess(Long transactionId, String rejectReason) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("transactionId", transactionId);
        result.put("rejectReason", rejectReason);
        result.put("status", "LOAN_REJECTED");
        result.put("message", "工行贷款申请已拒绝");
        
        return result;
    }

    /**
     * 【新增方法】查询并更新中台工作流节点
     * 模拟 OfTransactionServiceImpl.queryAndUpdateMiddleFlowNodeAndHander
     * 
     * @param transactionId 交易ID
     * @param flowNode 工作流节点
     * @return 更新结果
     */
    public Map<String, Object> queryAndUpdateMiddleFlowNodeAndHander(Long transactionId, String flowNode) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("transactionId", transactionId);
        result.put("flowNode", flowNode);
        result.put("updatedAt", "2026-02-02 10:00:00");
        result.put("message", "中台工作流节点更新成功");
        
        return result;
    }

    /**
     * 【关键逻辑变更】计算交易金额
     * 模拟 OfBalanceServiceImpl 的计算逻辑移除
     * 
     * 变更说明：移除了除以10000的计算逻辑
     * 旧逻辑: return amount.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
     * 新逻辑: return amount; (直接返回，不再除以10000)
     * 
     * @param amount 原始金额
     * @return 计算后的金额（不再除以10000）
     */
    public BigDecimal calculateTransactionAmount(BigDecimal amount) {
        // 【关键变更】移除了除以10000的逻辑
        // 旧代码: return amount.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
        return amount;  // 直接返回，不再转换
    }

    /**
     * 【方法重命名】查询单条交易详情
     * 模拟 IcbcFinanceService 的 queryDetailResult 重构为 querySingleDetailResult
     * 
     * 旧方法名: queryDetailResult(Long transactionId, String type)
     * 新方法名: querySingleDetailResult(Long transactionId)
     * 
     * @param transactionId 交易ID
     * @return 交易详情
     */
    public Map<String, Object> querySingleDetailResult(Long transactionId) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("transactionId", transactionId);
        result.put("status", "SUCCESS");
        result.put("amount", new BigDecimal("1000.00"));
        result.put("createTime", "2026-02-02 09:00:00");
        result.put("message", "查询成功");
        
        return result;
    }

    /**
     * 【查询逻辑优化】按时间倒序查询交易列表
     * 模拟 OfCertificationApplyServiceImpl 的排序优化
     * 
     * 变更说明：新增 createTime 倒序排序
     * 
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 交易列表
     */
    public List<Map<String, Object>> queryTransactionListOrderByCreateTimeDesc(
            Long userId, int pageNum, int pageSize) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 模拟返回按 createTime 倒序排列的数据
        Map<String, Object> item1 = new HashMap<>();
        item1.put("transactionId", 1001L);
        item1.put("createTime", "2026-02-02 10:00:00");  // 最新
        item1.put("amount", new BigDecimal("500.00"));
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("transactionId", 1000L);
        item2.put("createTime", "2026-02-01 09:00:00");  // 较旧
        item2.put("amount", new BigDecimal("300.00"));
        
        result.add(item1);
        result.add(item2);
        
        return result;
    }

    /**
     * 【业务场景新增】双确真工作流处理
     * 模拟 OfCheckAllService 的业务场景新增
     * 
     * 新增三种双确真工作流场景：
     * 1. DOUBLE_CHECK_APPROVE - 双确真审批通过
     * 2. DOUBLE_CHECK_REJECT - 双确真审批拒绝
     * 3. SUPPLIER_RESUBMIT - 供应商重新提交
     * 
     * @param transactionId 交易ID
     * @param workflowType 工作流类型
     * @param params 额外参数
     * @return 处理结果
     */
    public Map<String, Object> processDoubleCheckWorkflow(
            Long transactionId, String workflowType, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("transactionId", transactionId);
        result.put("workflowType", workflowType);
        
        // 【业务场景新增】根据工作流类型处理不同场景
        switch (workflowType) {
            case "DOUBLE_CHECK_APPROVE":
                // 双确真审批通过场景
                result.put("status", "APPROVED");
                result.put("nextNode", "FINAL_CONFIRM");
                result.put("message", "双确真审批通过");
                break;
                
            case "DOUBLE_CHECK_REJECT":
                // 双确真审批拒绝场景
                result.put("status", "REJECTED");
                result.put("nextNode", "SUPPLIER_MODIFY");
                result.put("rejectReason", params.get("rejectReason"));
                result.put("message", "双确真审批拒绝");
                break;
                
            case "SUPPLIER_RESUBMIT":
                // 供应商重新提交场景
                result.put("status", "RESUBMITTED");
                result.put("nextNode", "DOUBLE_CHECK_REVIEW");
                result.put("modifyContent", params.get("modifyContent"));
                result.put("message", "供应商重新提交成功");
                break;
                
            default:
                result.put("status", "ERROR");
                result.put("message", "未知的工作流类型");
        }
        
        return result;
    }

    /**
     * 【方法签名变更】启动工作流
     * 模拟 PlatAgwPushService.startProcess 的签名变更
     * 
     * 旧签名: startProcess(Long processId, String processType)
     * 新签名: startProcess(Long processId, String processType, Long transactionId)
     * 
     * @param processId 流程ID
     * @param processType 流程类型
     * @param transactionId 交易ID（新增参数）
     * @return 启动结果
     */
    public Map<String, Object> startProcess(Long processId, String processType, Long transactionId) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("processId", processId);
        result.put("processType", processType);
        result.put("transactionId", transactionId);  // 新增字段
        result.put("startTime", "2026-02-02 10:30:00");
        result.put("message", "工作流启动成功");
        
        return result;
    }
}
