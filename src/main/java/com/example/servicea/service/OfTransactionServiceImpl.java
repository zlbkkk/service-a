package com.example.servicea.service;

import com.example.common.service.OfTransactionService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 融资交易服务实现
 * 实现 common-api 中的 OfTransactionService 接口
 * 提供 Dubbo RPC 服务
 */
@Slf4j
@Service
public class OfTransactionServiceImpl implements OfTransactionService {

    /**
     * 查询主发票总数
     * 
     * 提交: 523bc2ae - feat(S26-169): 订单融资--提供两个总数查询接口
     * 变动: 新增 queryMainInvoiceTotal() 方法
     * 
     * @param companyId 企业ID
     * @return 主发票总数
     */
    @Override
    public Integer queryMainInvoiceTotal(String companyId) {
        log.info("[OfTransactionServiceImpl.queryMainInvoiceTotal] 查询主发票总数, companyId={}", companyId);
        
        // 参数校验
        if (companyId == null || companyId.trim().isEmpty()) {
            log.warn("[queryMainInvoiceTotal] 企业ID不能为空");
            throw new IllegalArgumentException("企业ID不能为空");
        }
        
        // 模拟从数据库查询主发票总数
        // 实际场景中会调用 Mapper 查询数据库
        // SELECT COUNT(*) FROM of_main_invoice WHERE company_id = #{companyId} AND deleted = 0
        
        Map<String, Integer> mockData = new HashMap<>();
        mockData.put("COMP001", 156);
        mockData.put("COMP002", 89);
        mockData.put("COMP003", 234);
        mockData.put("COMP004", 67);
        mockData.put("COMP005", 178);
        
        Integer total = mockData.getOrDefault(companyId, 0);
        
        log.info("[queryMainInvoiceTotal] 查询完成, companyId={}, total={}", companyId, total);
        return total;
    }
    
    /**
     * 查询收入发票总数
     * 
     * 提交: 523bc2ae - feat(S26-169): 订单融资--提供两个总数查询接口
     * 变动: 新增 queryIncomeInvoiceTotal() 方法
     * 
     * @param companyId 企业ID
     * @return 收入发票总数
     */
    @Override
    public Integer queryIncomeInvoiceTotal(String companyId) {
        log.info("[OfTransactionServiceImpl.queryIncomeInvoiceTotal] 查询收入发票总数, companyId={}", companyId);
        
        // 参数校验
        if (companyId == null || companyId.trim().isEmpty()) {
            log.warn("[queryIncomeInvoiceTotal] 企业ID不能为空");
            throw new IllegalArgumentException("企业ID不能为空");
        }
        
        // 模拟从数据库查询收入发票总数
        // 实际场景中会调用 Mapper 查询数据库
        // SELECT COUNT(*) FROM of_income_invoice WHERE company_id = #{companyId} AND deleted = 0
        
        Map<String, Integer> mockData = new HashMap<>();
        mockData.put("COMP001", 203);
        mockData.put("COMP002", 145);
        mockData.put("COMP003", 312);
        mockData.put("COMP004", 98);
        mockData.put("COMP005", 256);
        
        Integer total = mockData.getOrDefault(companyId, 0);
        
        log.info("[queryIncomeInvoiceTotal] 查询完成, companyId={}, total={}", companyId, total);
        return total;
    }
}
