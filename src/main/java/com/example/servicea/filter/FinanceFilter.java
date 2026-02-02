package com.example.servicea.filter;

import com.example.servicea.config.ApplyFinancingContext;

/**
 * 融资过滤器接口
 * 
 * @author system
 * @version 1.0
 */
public interface FinanceFilter {

    /**
     * 执行过滤
     * 
     * @param context 融资申请上下文
     * @return 是否通过
     */
    boolean doFilter(ApplyFinancingContext context);

    /**
     * 获取过滤器优先级
     * 
     * @return 优先级数值，越小越优先
     */
    int getOrder();
}
