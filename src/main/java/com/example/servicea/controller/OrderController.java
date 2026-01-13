package com.example.servicea.controller;

import com.example.common.dto.OrderDTO;
import com.example.servicea.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单控制器
 * 提供订单相关的 REST API
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping
    public OrderDTO createOrder(
            @RequestParam Long userId,
            @RequestParam String productName,
            @RequestParam BigDecimal amount) {
        return orderService.createOrder(userId, productName, amount);
    }

    @GetMapping("/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderDTO> getUserOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }

    @PutMapping("/{orderId}/status")
    public boolean updateStatus(
            @PathVariable Long orderId,
            @RequestParam Integer status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @DeleteMapping("/{orderId}")
    public boolean cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    /**
     * 获取订单状态描述
     * 场景1测试：修改返回值类型从 String 改为 OrderStatusResponse 对象
     */
    @GetMapping("/{orderId}/status-text")
    public OrderStatusResponse getOrderStatusText(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            return new OrderStatusResponse("订单不存在", -1, null);
        }
        
        // 根据状态码返回详细状态信息
        String statusText;
        String statusDescription;
        switch (order.getStatus()) {
            case 0:
                statusText = "待支付";
                statusDescription = "订单已创建，等待用户支付";
                break;
            case 1:
                statusText = "已支付";
                statusDescription = "订单已完成支付，正在处理中";
                break;
            case 2:
                statusText = "已取消";
                statusDescription = "订单已被取消";
                break;
            default:
                statusText = "未知状态";
                statusDescription = "订单状态异常";
        }
        
        return new OrderStatusResponse(statusText, order.getStatus(), statusDescription);
    }
    
    /**
     * 订单状态响应对象
     * 场景1测试：新增返回值类型
     */
    public static class OrderStatusResponse {
        private String statusText;
        private Integer statusCode;
        private String description;
        
        public OrderStatusResponse() {}
        
        public OrderStatusResponse(String statusText, Integer statusCode, String description) {
            this.statusText = statusText;
            this.statusCode = statusCode;
            this.description = description;
        }
        
        public String getStatusText() { return statusText; }
        public void setStatusText(String statusText) { this.statusText = statusText; }
        public Integer getStatusCode() { return statusCode; }
        public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    
    /**
     * 获取订单汇总信息
     * 前端UI测试：模拟前端项目 beehive-order-finance-frontend 的 getOrderSummary 调用
     * 前端页面：资产管理 > 订单管理 (orderManage.vue)
     * 前端调用：orderApi.ofOrderController.getOrderSummary()
     */
    @PostMapping("/summary")
    public OrderSummaryResponse getOrderSummary(@RequestBody OrderSummaryRequest request) {
        // 模拟查询订单汇总数据
        OrderSummaryResponse response = new OrderSummaryResponse();
        response.setSumCount(100);
        response.setSumAmount(new BigDecimal("1000000.00"));
        response.setApplySumCount(50);
        response.setApplySumAmount(new BigDecimal("500000.00"));
        return response;
    }
    
    /**
     * 订单汇总请求对象
     */
    public static class OrderSummaryRequest {
        private String fuzzySourceOrderNo;
        private Long buyerCompanyId;
        private Long sellerCompanyId;
        private String fuzzyContractNo;
        private String queryStartDay;
        private String queryEndDay;
        
        public String getFuzzySourceOrderNo() { return fuzzySourceOrderNo; }
        public void setFuzzySourceOrderNo(String fuzzySourceOrderNo) { this.fuzzySourceOrderNo = fuzzySourceOrderNo; }
        public Long getBuyerCompanyId() { return buyerCompanyId; }
        public void setBuyerCompanyId(Long buyerCompanyId) { this.buyerCompanyId = buyerCompanyId; }
        public Long getSellerCompanyId() { return sellerCompanyId; }
        public void setSellerCompanyId(Long sellerCompanyId) { this.sellerCompanyId = sellerCompanyId; }
        public String getFuzzyContractNo() { return fuzzyContractNo; }
        public void setFuzzyContractNo(String fuzzyContractNo) { this.fuzzyContractNo = fuzzyContractNo; }
        public String getQueryStartDay() { return queryStartDay; }
        public void setQueryStartDay(String queryStartDay) { this.queryStartDay = queryStartDay; }
        public String getQueryEndDay() { return queryEndDay; }
        public void setQueryEndDay(String queryEndDay) { this.queryEndDay = queryEndDay; }
    }
    
    /**
     * 订单汇总响应对象
     */
    public static class OrderSummaryResponse {
        private Integer sumCount;
        private BigDecimal sumAmount;
        private Integer applySumCount;
        private BigDecimal applySumAmount;
        
        public Integer getSumCount() { return sumCount; }
        public void setSumCount(Integer sumCount) { this.sumCount = sumCount; }
        public BigDecimal getSumAmount() { return sumAmount; }
        public void setSumAmount(BigDecimal sumAmount) { this.sumAmount = sumAmount; }
        public Integer getApplySumCount() { return applySumCount; }
        public void setApplySumCount(Integer applySumCount) { this.applySumCount = applySumCount; }
        public BigDecimal getApplySumAmount() { return applySumAmount; }
        public void setApplySumAmount(BigDecimal applySumAmount) { this.applySumAmount = applySumAmount; }
    }
}
