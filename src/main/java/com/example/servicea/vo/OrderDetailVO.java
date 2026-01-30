package com.example.servicea.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情视图对象
 * 测试类型: VO - 新增类 (模拟 Task #110 中 OfBalanceOccupyDetailVO 的变更)
 */
@Data
public class OrderDetailVO {
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 订单名称
     */
    private String orderName;
    
    /**
     * 订单金额
     */
    private BigDecimal amount;
    
    /**
     * 订单状态
     * 可选值: PENDING(待处理), APPROVED(已审批), REJECTED(已拒绝), COMPLETED(已完成)
     */
    private String orderStatus;
    
    /**
     * 审批节点
     */
    private String approvalNode;
    
    /**
     * 经办人
     */
    private String handler;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
