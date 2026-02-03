package com.example.servicea.provider;

import com.example.servicea.mapper.OfTransactionMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Dubbo Provider
 */
@Service
public class OfTransactionProvider2 {

    @Autowired
    private OfTransactionMapper2 ofTransactionMapper2;

    /**
     * 分页查询融资交易列表
     */
    public List<Map<String, Object>> page(Map<String, Object> params) {
        return ofTransactionMapper2.page(params);
    }
}
