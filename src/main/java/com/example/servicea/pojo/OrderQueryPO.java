package com.example.servicea.pojo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单查询持久化对象
 * 测试类型: PO - 新增类 (模拟 Task #110 中 IcbcQueryDetailPO 的变更)
 */
@Data
public class OrderQueryPO {
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
    
    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 手续费
     */
    private BigDecimal feeAmount;
    
    /**
     * 订单状态
     */
    private String status;
}
