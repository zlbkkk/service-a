package com.example.servicea.filter;

import com.example.servicea.config.ApplyFinancingContext;
import org.springframework.stereotype.Component;

/**
 * 【新增过滤器类】融资申请确真校验过滤器
 * 
 * 模拟 CeConfirmFinanceThrowExFilter 的变更类型
 * 用于测试新增过滤器类的识别
 * 
 * @author system
 * @version 1.0
 */
@Component
public class CeConfirmFinanceThrowExFilter implements FinanceFilter {

    /**
     * 执行确真校验过滤
     * 
     * @param context 融资申请上下文
     * @return 是否通过校验
     */
    @Override
    public boolean doFilter(ApplyFinancingContext context) {
        // 校验确真模式
        String confirmMode = context.getCeConfirmMode();
        if (confirmMode == null || confirmMode.isEmpty()) {
            throw new RuntimeException("确真模式不能为空");
        }

        // 校验确真级别
        String confirmLevel = context.getCeConfirmLevel();
        if (confirmLevel == null || confirmLevel.isEmpty()) {
            throw new RuntimeException("确真级别不能为空");
        }

        // 单次确真模式校验
        if ("SINGLE".equals(confirmMode)) {
            return validateSingleConfirm(context);
        }

        // 双确真模式校验
        if ("DOUBLE".equals(confirmMode)) {
            return validateDoubleConfirm(context);
        }

        throw new RuntimeException("不支持的确真模式: " + confirmMode);
    }

    /**
     * 单次确真校验
     */
    private boolean validateSingleConfirm(ApplyFinancingContext context) {
        // 单次确真需要集团公司ID
        if (context.getCeGroupCompanyId() == null) {
            throw new RuntimeException("单次确真模式下集团公司ID不能为空");
        }
        return true;
    }

    /**
     * 双确真校验
     */
    private boolean validateDoubleConfirm(ApplyFinancingContext context) {
        // 双确真需要集团公司ID和集团公司信息
        if (context.getCeGroupCompanyId() == null) {
            throw new RuntimeException("双确真模式下集团公司ID不能为空");
        }
        if (context.getCeGroupCompanyDO() == null || context.getCeGroupCompanyDO().isEmpty()) {
            throw new RuntimeException("双确真模式下集团公司信息不能为空");
        }
        return true;
    }

    @Override
    public int getOrder() {
        return 100; // 优先级
    }
}
