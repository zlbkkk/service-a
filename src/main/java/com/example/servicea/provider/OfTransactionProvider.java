package com.example.servicea.provider;

import com.example.common.service.OfTransactionService;
import com.example.servicea.service.OfTransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 融资交易 Dubbo Provider
 * 暴露 OfTransactionService 接口给其他服务调用
 */
@Component
public class OfTransactionProvider implements OfTransactionService {

    @Autowired
    private OfTransactionServiceImpl ofTransactionService;

    @Override
    public Integer queryMainInvoiceTotal(String companyId) {
        return ofTransactionService.queryMainInvoiceTotal(companyId);
    }

    @Override
    public Integer queryIncomeInvoiceTotal(String companyId) {
        return ofTransactionService.queryIncomeInvoiceTotal(companyId);
    }
}
