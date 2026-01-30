package com.example.servicea.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * 测试类型: DO (实体类) - 字段新增
 * 模拟 Task #110 中 OfTransactionDO 的变更模式
 */
@Data
public class OrderDO {
    
    private Long id;
    
    private String orderNo;
    
    private String orderName;
    
    private BigDecimal amount;
    
    private String status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    // ========== 新增字段 (模拟 Task #110 中 OfTransactionDO 的变更) ==========
    
    /**
     * 核企集团公司ID
     * 用于支持双确真场景
     */
    private Long ceGroupCompanyId;
    
    /**
     * 核企集团公司名称
     */
    private String ceGroupCompanyName;
    
    /**
     * 核企确真方式
     * 可选值: SINGLE(单确真), DOUBLE(双确真)
     */
    private String ceConfirmMode;
    
    /**
     * 核企确真层级
     * 可选值: FIRST(一级), SECOND(二级)
     */
    private String ceConfirmLevel;
    
    /**
     * 网关流程审核节点
     */
    private String agwCheckNode;
    
    /**
     * 网关经办人
     */
    private String agwHandler;
}
