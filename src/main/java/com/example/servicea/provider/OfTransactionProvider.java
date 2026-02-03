package com.example.servicea.provider;

import com.example.servicea.mapper.OfTransactionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * 融资交易 Dubbo Provider
 * 对外提供融资交易数据访问服务
 * 
 * 该 Provider 被 service-b 的 OfTransactionService 调用
 * 调用链：
 * 前端 -> service-b OfTransactionController 
 *      -> service-b OfTransactionService 
 *      -> service-a OfTransactionProvider (Dubbo)
 *      -> service-a OfTransactionMapper
 */
@Service
public class OfTransactionProvider {

    @Autowired
    private OfTransactionMapper ofTransactionMapper;

    /**
     * 分页查询交易列表
     * @param params 查询参数
     * @return 分页数据
     */
    public List<Map<String, Object>> page(Map<String, Object> params) {
        return ofTransactionMapper.page(params);
    }

    /**
     * 获取融资汇总信息
     * @return 汇总信息
     */
    public Map<String, Object> getTransactionSumInfo() {
        return ofTransactionMapper.getTransactionSumInfo();
    }

    /**
     * 获取交易详情
     * @param transactionNo 交易编号
     * @return 交易详情
     */
    public Map<String, Object> getTransactionDetail(String transactionNo) {
        return ofTransactionMapper.getTransactionDetail(transactionNo);
    }

    /**
     * 获取供应商上一次回款账户信息
     * @return 账户信息
     */
    public Map<String, Object> getSpyLastReturnAccount() {
        return ofTransactionMapper.getSpyLastReturnAccount();
    }

    /**
     * 更新融资信息
     * @param transactionInfo 融资信息
     * @return 更新结果
     */
    public boolean updateTransactionInfo(Map<String, Object> transactionInfo) {
        int rows = ofTransactionMapper.updateTransactionInfo(transactionInfo);
        return rows > 0;
    }

    /**
     * 批量删除交易
     * @param transactionNos 交易编号列表
     * @return 删除结果
     */
    public boolean batchDel(List<String> transactionNos) {
        int rows = ofTransactionMapper.batchDel(transactionNos);
        return rows > 0;
    }

    /**
     * 将单次确真字段置空
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public boolean setNull4SingleCeConfirm(Long transactionId) {
        int rows = ofTransactionMapper.setNull4SingleCeConfirm(transactionId);
        return rows > 0;
    }

    /**
     * 将双确真字段置空
     * @param transactionId 交易ID
     * @return 操作结果
     */
    public boolean setNull4DoubleCeConfirm(Long transactionId) {
        int rows = ofTransactionMapper.setNull4DoubleCeConfirm(transactionId);
        return rows > 0;
    }
}
