package com.example.servicea.provider;

import com.example.servicea.config.ApplyFinancingContext;
import com.example.servicea.handler.CeConfirmCheckApplyChainHandler;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资过滤器链 Dubbo 提供者
 * 
 * 暴露配置类/过滤器/处理器的功能为 Dubbo 服务
 * 用于测试配置类变更的跨项目影响识别
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 10000, retries = 2)
@Component
public class FinanceChainProvider {

    @Autowired
    private CeConfirmCheckApplyChainHandler ceConfirmHandler;

    /**
     * 执行双确真校验
     * 
     * 调用链：
     * FinanceChainProvider → CeConfirmCheckApplyChainHandler 
     *                      → FinanceChain → CeConfirmFinanceThrowExFilter
     * 
     * @param transactionId 交易ID
     * @param orderId 订单ID
     * @param userId 用户ID
     * @param amount 金额
     * @param ceGroupCompanyId 集团公司ID（新增字段）
     * @param ceGroupCompanyDO 集团公司信息（新增字段）
     * @return 校验结果
     */
    public Map<String, Object> executeDoubleConfirmCheck(
            Long transactionId,
            Long orderId,
            Long userId,
            BigDecimal amount,
            Long ceGroupCompanyId,
            Map<String, Object> ceGroupCompanyDO) {

        // 构建上下文对象（使用新增字段）
        ApplyFinancingContext context = new ApplyFinancingContext();
        context.setTransactionId(transactionId);
        context.setOrderId(orderId);
        context.setUserId(userId);
        context.setAmount(amount);
        context.setCeGroupCompanyId(ceGroupCompanyId);  // 新增字段
        context.setCeGroupCompanyDO(ceGroupCompanyDO);  // 新增字段

        // 调用处理器
        return ceConfirmHandler.handle(context);
    }

    /**
     * 执行单次确真校验
     * 
     * @param transactionId 交易ID
     * @param amount 金额
     * @param ceGroupCompanyId 集团公司ID
     * @return 校验结果
     */
    public Map<String, Object> executeSingleConfirmCheck(
            Long transactionId,
            BigDecimal amount,
            Long ceGroupCompanyId) {

        ApplyFinancingContext context = new ApplyFinancingContext();
        context.setTransactionId(transactionId);
        context.setAmount(amount);
        context.setCeGroupCompanyId(ceGroupCompanyId);
        context.setCeConfirmMode("SINGLE");  // 设置为单次确真

        return ceConfirmHandler.handle(context);
    }

    /**
     * 获取确真模式建议
     * 
     * 根据金额和集团信息推荐确真模式
     * 
     * @param amount 金额
     * @param hasGroupCompany 是否有集团公司
     * @return 建议的确真模式
     */
    public Map<String, Object> getConfirmModeSuggestion(BigDecimal amount, boolean hasGroupCompany) {
        Map<String, Object> result = new HashMap<>();

        // 确真模式判断
        String confirmMode;
        if (hasGroupCompany) {
            confirmMode = "DOUBLE";
        } else {
            confirmMode = "SINGLE";
        }

        // 确真级别判断
        String confirmLevel;
        if (amount.doubleValue() >= 1000000) {
            confirmLevel = "L3";
        } else if (amount.doubleValue() >= 100000) {
            confirmLevel = "L2";
        } else {
            confirmLevel = "L1";
        }

        result.put("confirmMode", confirmMode);
        result.put("confirmLevel", confirmLevel);
        result.put("amount", amount);
        result.put("message", String.format("建议使用 %s 模式，级别 %s", confirmMode, confirmLevel));

        return result;
    }
}
