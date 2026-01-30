package com.example.servicea.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单导出DTO
 * 测试类型: DTO - 注解修改 (单位从"万元"改为"元")
 */
@Data
public class OrderExportDTO {
    
    @ExcelProperty("订单编号")
    private String orderNo;
    
    @ExcelProperty("订单名称")
    private String orderName;
    
    // ========== 注解修改 (模拟 Task #110 中 CommonCeBalanceExportDTO 的变更) ==========
    // 原来: @ExcelProperty("订单金额（万元）")
    // 现在: @ExcelProperty("订单金额（元）")
    
    @ExcelProperty("订单金额（元）")
    private BigDecimal amount;
    
    @ExcelProperty("已支付金额（元）")
    private BigDecimal paidAmount;
    
    @ExcelProperty("剩余金额（元）")
    private BigDecimal remainingAmount;
    
    @ExcelProperty("订单状态")
    private String status;
}
