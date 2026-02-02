package com.example.servicea.handler;

import com.example.servicea.chain.FinanceChain;
import com.example.servicea.config.ApplyFinancingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 【新增处理器类】双确真校验处理器
 * 
 * 模拟 CeConfirmCheckApplyChainHandler 的变更类型
 * 用于测试新增处理器类的识别
 * 
 * @author system
 * @version 1.0
 */
@Component
public class CeConfirmCheckApplyChainHandler {

    @Autowired
    private FinanceChain financeChain;

    /**
     * 处理双确真校验申请
     * 
     * @param context 融资申请上下文
     * @return 处理结果
     */
    public Map<String, Object> handle(ApplyFinancingContext context) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 初始化过滤器链
            financeChain.init();

            // 执行过滤器链
            boolean passed = financeChain.execute(context);

            result.put("success", passed);
            result.put("transactionId", context.getTransactionId());
            result.put("confirmMode", context.getCeConfirmMode());
            result.put("confirmLevel", context.getCeConfirmLevel());
            result.put("message", passed ? "双确真校验通过" : "双确真校验失败");

        } catch (Exception e) {
            result.put("success", false);
            result.put("transactionId", context.getTransactionId());
            result.put("error", e.getMessage());
            result.put("message", "双确真校验异常: " + e.getMessage());
        }

        return result;
    }

    /**
     * 批量处理双确真校验
     * 
     * @param contexts 融资申请上下文列表
     * @return 批量处理结果
     */
    public Map<String, Object> batchHandle(java.util.List<ApplyFinancingContext> contexts) {
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;

        for (ApplyFinancingContext context : contexts) {
            Map<String, Object> singleResult = handle(context);
            if (Boolean.TRUE.equals(singleResult.get("success"))) {
                successCount++;
            } else {
                failCount++;
            }
        }

        result.put("total", contexts.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("message", String.format("批量处理完成: 成功 %d, 失败 %d", successCount, failCount));

        return result;
    }
}
