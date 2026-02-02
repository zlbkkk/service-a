package com.example.servicea.filter;

import com.example.servicea.config.ApplyFinancingContext;
import org.springframework.stereotype.Component;

/**
 * 【逻辑增强】工作流融资校验过滤器
 * 
 * 模拟 WorkFlowFinanceThrowExFilter 的变更类型
 * 用于测试过滤器逻辑增强的识别
 * 
 * @author system
 * @version 1.0
 */
@Component
public class WorkFlowFinanceThrowExFilter implements FinanceFilter {

    /**
     * 执行工作流校验过滤
     * 
     * @param context 融资申请上下文
     * @return 是否通过校验
     */
    @Override
    public boolean doFilter(ApplyFinancingContext context) {
        // 基础校验
        if (context.getTransactionId() == null) {
            throw new RuntimeException("交易ID不能为空");
        }

        if (context.getAmount() == null || context.getAmount().doubleValue() <= 0) {
            throw new RuntimeException("融资金额必须大于0");
        }

        // 【新增逻辑】设置确真模式字段
        // 根据集团公司信息判断确真模式
        if (context.getCeGroupCompanyDO() != null && !context.getCeGroupCompanyDO().isEmpty()) {
            // 有集团公司信息，使用双确真模式
            context.setCeConfirmMode("DOUBLE");
        } else if (context.getCeGroupCompanyId() != null) {
            // 只有集团公司ID，使用单次确真模式
            context.setCeConfirmMode("SINGLE");
        } else {
            // 无集团信息，不需要确真
            context.setCeConfirmMode("NONE");
        }

        // 【新增逻辑】设置确真级别
        if (context.getAmount().doubleValue() >= 1000000) {
            context.setCeConfirmLevel("L3"); // 高级别
        } else if (context.getAmount().doubleValue() >= 100000) {
            context.setCeConfirmLevel("L2"); // 中级别
        } else {
            context.setCeConfirmLevel("L1"); // 低级别
        }

        return true;
    }

    @Override
    public int getOrder() {
        return 50; // 工作流过滤器优先级较高
    }
}
