package com.example.servicea.provider;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务 Dubbo 提供者
 * 
 * 提供支付相关的 RPC 接口
 * 供其他微服务通过 Dubbo 调用
 * 
 * @author system
 * @version 1.0
 */
@DubboService(version = "1.0.0", timeout = 10000, retries = 2)
@Component
public class PaymentProvider {

    /**
     * 创建支付订单
     * 
     * @param orderId 订单ID
     * @param amount 支付金额
     * @param paymentMethod 支付方式 (ALIPAY/WECHAT/BANK)
     * @return 支付结果
     */
    public Map<String, Object> createPayment(Long orderId, BigDecimal amount, String paymentMethod) {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟创建支付订单
        String paymentId = "PAY-" + System.currentTimeMillis();
        
        result.put("success", true);
        result.put("paymentId", paymentId);
        result.put("orderId", orderId);
        result.put("amount", amount);
        result.put("paymentMethod", paymentMethod);
        result.put("status", "PENDING");
        result.put("message", "支付订单创建成功");
        
        return result;
    }

    /**
     * 查询支付状态
     * 
     * @param paymentId 支付ID
     * @return 支付状态信息
     */
    public Map<String, Object> queryPaymentStatus(String paymentId) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("paymentId", paymentId);
        result.put("status", "SUCCESS");
        result.put("paidTime", "2026-01-14 10:35:00");
        result.put("message", "支付成功");
        
        return result;
    }

    /**
     * 退款处理（修改：增加操作人参数）
     * 
     * @param paymentId 支付ID
     * @param refundAmount 退款金额
     * @param reason 退款原因
     * @param operator 操作人
     * @return 退款结果
     */
    public Map<String, Object> processRefund(String paymentId, BigDecimal refundAmount, String reason, String operator) {
        Map<String, Object> result = new HashMap<>();
        
        String refundId = "REFUND-" + System.currentTimeMillis();
        
        result.put("success", true);
        result.put("refundId", refundId);
        result.put("paymentId", paymentId);
        result.put("refundAmount", refundAmount);
        result.put("reason", reason);
        result.put("operator", operator);  // 新增：记录操作人
        result.put("operateTime", "2026-01-14 15:00:00");  // 新增：操作时间
        result.put("status", "PROCESSING");
        result.put("message", "退款申请已提交");
        
        return result;
    }

    /**
     * 查询退款状态（新增方法）
     * 
     * @param refundId 退款ID
     * @return 退款状态信息
     */
    public Map<String, Object> queryRefundStatus(String refundId) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("refundId", refundId);
        result.put("status", "SUCCESS");
        result.put("refundTime", "2026-01-14 15:30:00");
        result.put("message", "退款成功");
        
        return result;
    }

    /**
     * 取消支付（新增方法）
     * 
     * @param paymentId 支付ID
     * @param reason 取消原因
     * @return 取消结果
     */
    public Map<String, Object> cancelPayment(String paymentId, String reason) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        result.put("paymentId", paymentId);
        result.put("reason", reason);
        result.put("cancelTime", "2026-01-14 16:00:00");
        result.put("message", "支付已取消");
        
        return result;
    }
}
