package com.example.servicea.config;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 【字段新增】融资申请上下文
 * 
 * 模拟 ApplyFinancingContext 的变更类型
 * 用于测试上下文对象字段新增的识别
 * 
 * @author system
 * @version 1.0
 */
public class ApplyFinancingContext implements Serializable {

    private static final long serialVersionUID = 1L;

    // 基础字段
    private Long transactionId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;

    // 【新增字段】集团公司ID
    private Long ceGroupCompanyId;

    // 【新增字段】集团公司信息对象
    private Map<String, Object> ceGroupCompanyDO;

    // 【新增字段】确真模式（SINGLE/DOUBLE）
    private String ceConfirmMode;

    // 【新增字段】确真级别（L1/L2/L3）
    private String ceConfirmLevel;

    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 【新增字段】Getters and Setters
    public Long getCeGroupCompanyId() {
        return ceGroupCompanyId;
    }

    public void setCeGroupCompanyId(Long ceGroupCompanyId) {
        this.ceGroupCompanyId = ceGroupCompanyId;
    }

    public Map<String, Object> getCeGroupCompanyDO() {
        return ceGroupCompanyDO;
    }

    public void setCeGroupCompanyDO(Map<String, Object> ceGroupCompanyDO) {
        this.ceGroupCompanyDO = ceGroupCompanyDO;
    }

    public String getCeConfirmMode() {
        return ceConfirmMode;
    }

    public void setCeConfirmMode(String ceConfirmMode) {
        this.ceConfirmMode = ceConfirmMode;
    }

    public String getCeConfirmLevel() {
        return ceConfirmLevel;
    }

    public void setCeConfirmLevel(String ceConfirmLevel) {
        this.ceConfirmLevel = ceConfirmLevel;
    }
}
