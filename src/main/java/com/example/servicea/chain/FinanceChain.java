package com.example.servicea.chain;

import com.example.servicea.config.ApplyFinancingContext;
import com.example.servicea.filter.CeConfirmFinanceThrowExFilter;
import com.example.servicea.filter.FinanceFilter;
import com.example.servicea.filter.WorkFlowFinanceThrowExFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 【过滤器新增】融资过滤器链
 * 
 * 模拟 FinanceChain 的变更类型
 * 用于测试过滤器链变更的识别
 * 
 * @author system
 * @version 1.0
 */
@Component
public class FinanceChain {

    private final List<FinanceFilter> filters = new ArrayList<>();

    @Autowired
    private WorkFlowFinanceThrowExFilter workFlowFilter;

    // 【新增过滤器】确真校验过滤器
    @Autowired
    private CeConfirmFinanceThrowExFilter ceConfirmFilter;

    /**
     * 初始化过滤器链
     */
    public void init() {
        filters.clear();
        
        // 添加工作流过滤器
        filters.add(workFlowFilter);
        
        // 【新增】添加确真校验过滤器
        filters.add(ceConfirmFilter);
        
        // 按优先级排序
        filters.sort(Comparator.comparingInt(FinanceFilter::getOrder));
    }

    /**
     * 执行过滤器链
     * 
     * @param context 融资申请上下文
     * @return 是否全部通过
     */
    public boolean execute(ApplyFinancingContext context) {
        for (FinanceFilter filter : filters) {
            if (!filter.doFilter(context)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取过滤器数量
     */
    public int getFilterCount() {
        return filters.size();
    }
}
