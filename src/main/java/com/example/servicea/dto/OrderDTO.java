package com.example.servicea.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单数据传输对象
 * 测试类型: DTO - 字段新增
 */
@Data
public class OrderDTO {
    
    private Long id;
    
    private String orderNo;
    
    private String orderName;
    
    private BigDecimal amount;
    
    private String status;
    
    private LocalDateTime createTime;
    
    // ========== 新增字段 (模拟 Task #110 中 OfTransactionDTO 的变更) ==========
    
    /**
     * 核企集团公司ID
     */
    private Long ceGroupCompanyId;
    
    /**
     * 核企集团公司名称
     */
    private String ceGroupCompanyName;
    
    /**
     * 网关流程审核节点
     */
    private String agwCheckNode;
    
    /**
     * 网关经办人
     */
    private String agwHandler;
    
    /**
     * 创建时间开始 (查询条件)
     */
    private LocalDateTime createTimeStart;
    
    /**
     * 创建时间结束 (查询条件)
     */
    private LocalDateTime createTimeEnd;
}
