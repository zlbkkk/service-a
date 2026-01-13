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
     * 
     * 修改说明：
     * 1. 增加数据验证逻辑
     * 2. 增加日期范围校验
     * 3. 增加缓存查询逻辑
     * 4. 增加异常处理
     * 5. 增加日志记录
     * 6. 增加数据统计维度（按状态分组）
     */
    @PostMapping("/summary")
    public OrderSummaryResponse getOrderSummary(@RequestBody OrderSummaryRequest request) {
        // 1. 参数验证
        if (request == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }
        
        // 2. 日期范围校验
        if (request.getQueryStartDay() != null && request.getQueryEndDay() != null) {
            // 校验日期格式和范围
            if (request.getQueryStartDay().compareTo(request.getQueryEndDay()) > 0) {
                throw new IllegalArgumentException("开始日期不能大于结束日期");
            }
        }
        
        // 3. 构建查询条件日志
        StringBuilder queryLog = new StringBuilder("订单汇总查询 - ");
        if (request.getFuzzySourceOrderNo() != null) {
            queryLog.append("订单号:").append(request.getFuzzySourceOrderNo()).append(", ");
        }
        if (request.getBuyerCompanyId() != null) {
            queryLog.append("买方公司ID:").append(request.getBuyerCompanyId()).append(", ");
        }
        if (request.getSellerCompanyId() != null) {
            queryLog.append("卖方公司ID:").append(request.getSellerCompanyId()).append(", ");
        }
        System.out.println(queryLog.toString());
        
        // 4. 模拟缓存查询（实际项目中会查询Redis）
        String cacheKey = generateCacheKey(request);
        System.out.println("缓存Key: " + cacheKey);
        
        // 5. 模拟数据库查询订单汇总数据
        OrderSummaryResponse response = new OrderSummaryResponse();
        
        // 基础统计
        response.setSumCount(100);
        response.setSumAmount(new BigDecimal("1000000.00"));
        response.setApplySumCount(50);
        response.setApplySumAmount(new BigDecimal("500000.00"));
        
        // 6. 新增：按状态分组统计
        response.setPendingCount(30);
        response.setPendingAmount(new BigDecimal("300000.00"));
        response.setPaidCount(50);
        response.setPaidAmount(new BigDecimal("500000.00"));
        response.setCancelledCount(20);
        response.setCancelledAmount(new BigDecimal("200000.00"));
        
        // 7. 新增：时间范围统计
        if (request.getQueryStartDay() != null && request.getQueryEndDay() != null) {
            response.setQueryStartDay(request.getQueryStartDay());
            response.setQueryEndDay(request.getQueryEndDay());
            response.setDayCount(calculateDayCount(request.getQueryStartDay(), request.getQueryEndDay()));
        }
        
        // 8. 新增：平均订单金额
        if (response.getSumCount() > 0) {
            BigDecimal avgAmount = response.getSumAmount().divide(
                new BigDecimal(response.getSumCount()), 2, BigDecimal.ROUND_HALF_UP);
            response.setAvgOrderAmount(avgAmount);
        }
        
        // 9. 记录查询结果日志
        System.out.println("查询结果 - 总订单数: " + response.getSumCount() + 
                         ", 总金额: " + response.getSumAmount() +
                         ", 平均金额: " + response.getAvgOrderAmount());
        
        return response;
    }
    
    /**
     * 生成缓存Key
     */
    private String generateCacheKey(OrderSummaryRequest request) {
        return "order:summary:" + 
               (request.getBuyerCompanyId() != null ? request.getBuyerCompanyId() : "all") + ":" +
               (request.getSellerCompanyId() != null ? request.getSellerCompanyId() : "all") + ":" +
               (request.getQueryStartDay() != null ? request.getQueryStartDay() : "all") + ":" +
               (request.getQueryEndDay() != null ? request.getQueryEndDay() : "all");
    }
    
    /**
     * 计算日期范围天数
     */
    private int calculateDayCount(String startDay, String endDay) {
        // 简化实现，实际项目中会使用日期工具类
        try {
            return Math.abs(Integer.parseInt(endDay.replace("-", "")) - 
                          Integer.parseInt(startDay.replace("-", "")));
        } catch (Exception e) {
            return 0;
        }
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
     * 新增字段：
     * - 按状态分组统计（待支付、已支付、已取消）
     * - 时间范围信息
     * - 平均订单金额
     */
    public static class OrderSummaryResponse {
        private Integer sumCount;
        private BigDecimal sumAmount;
        private Integer applySumCount;
        private BigDecimal applySumAmount;
        
        // 新增：按状态分组统计
        private Integer pendingCount;      // 待支付订单数
        private BigDecimal pendingAmount;  // 待支付金额
        private Integer paidCount;         // 已支付订单数
        private BigDecimal paidAmount;     // 已支付金额
        private Integer cancelledCount;    // 已取消订单数
        private BigDecimal cancelledAmount; // 已取消金额
        
        // 新增：时间范围信息
        private String queryStartDay;
        private String queryEndDay;
        private Integer dayCount;          // 查询天数
        
        // 新增：平均订单金额
        private BigDecimal avgOrderAmount;
        
        public Integer getSumCount() { return sumCount; }
        public void setSumCount(Integer sumCount) { this.sumCount = sumCount; }
        public BigDecimal getSumAmount() { return sumAmount; }
        public void setSumAmount(BigDecimal sumAmount) { this.sumAmount = sumAmount; }
        public Integer getApplySumCount() { return applySumCount; }
        public void setApplySumCount(Integer applySumCount) { this.applySumCount = applySumCount; }
        public BigDecimal getApplySumAmount() { return applySumAmount; }
        public void setApplySumAmount(BigDecimal applySumAmount) { this.applySumAmount = applySumAmount; }
        
        public Integer getPendingCount() { return pendingCount; }
        public void setPendingCount(Integer pendingCount) { this.pendingCount = pendingCount; }
        public BigDecimal getPendingAmount() { return pendingAmount; }
        public void setPendingAmount(BigDecimal pendingAmount) { this.pendingAmount = pendingAmount; }
        public Integer getPaidCount() { return paidCount; }
        public void setPaidCount(Integer paidCount) { this.paidCount = paidCount; }
        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
        public Integer getCancelledCount() { return cancelledCount; }
        public void setCancelledCount(Integer cancelledCount) { this.cancelledCount = cancelledCount; }
        public BigDecimal getCancelledAmount() { return cancelledAmount; }
        public void setCancelledAmount(BigDecimal cancelledAmount) { this.cancelledAmount = cancelledAmount; }
        
        public String getQueryStartDay() { return queryStartDay; }
        public void setQueryStartDay(String queryStartDay) { this.queryStartDay = queryStartDay; }
        public String getQueryEndDay() { return queryEndDay; }
        public void setQueryEndDay(String queryEndDay) { this.queryEndDay = queryEndDay; }
        public Integer getDayCount() { return dayCount; }
        public void setDayCount(Integer dayCount) { this.dayCount = dayCount; }
        
        public BigDecimal getAvgOrderAmount() { return avgOrderAmount; }
        public void setAvgOrderAmount(BigDecimal avgOrderAmount) { this.avgOrderAmount = avgOrderAmount; }
    }
    
    /**
     * 获取订单统计报表
     * 前端UI测试：模拟前端项目 beehive-order-finance-frontend 的 getOrderStatistics 调用
     * 前端页面：资产管理 > 订单统计 (orderStatistics.vue)
     * 前端调用：orderApi.ofOrderController.getOrderStatistics()
     * 
     * 新增接口说明：
     * 1. 提供订单统计数据，包含多维度分析
     * 2. 支持按时间范围、状态、金额区间统计
     * 3. 返回图表所需的数据格式
     */
    @PostMapping("/statistics")
    public OrderStatisticsResponse getOrderStatistics(@RequestBody OrderStatisticsRequest request) {
        // 1. 参数验证
        if (request == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }
        
        // 2. 日期范围验证
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().compareTo(request.getEndDate()) > 0) {
                throw new IllegalArgumentException("开始日期不能大于结束日期");
            }
        }
        
        // 3. 构建统计数据
        OrderStatisticsResponse response = new OrderStatisticsResponse();
        
        // 基础统计
        response.setTotalOrders(500);
        response.setTotalAmount(new BigDecimal("5000000.00"));
        response.setAvgOrderAmount(new BigDecimal("10000.00"));
        
        // 按状态统计
        response.setPendingOrders(150);
        response.setPaidOrders(250);
        response.setCancelledOrders(100);
        
        // 按金额区间统计
        response.setSmallOrders(200);  // 0-5000
        response.setMediumOrders(200); // 5000-20000
        response.setLargeOrders(100);  // 20000+
        
        // 趋势数据（最近7天）
        response.setDailyOrderCounts(new int[]{50, 60, 55, 70, 65, 80, 75});
        response.setDailyAmounts(new double[]{500000, 600000, 550000, 700000, 650000, 800000, 750000});
        
        // 4. 记录日志
        System.out.println("订单统计查询 - 总订单数: " + response.getTotalOrders() + 
                         ", 总金额: " + response.getTotalAmount());
        
        return response;
    }
    
    /**
     * 订单统计请求对象
     */
    public static class OrderStatisticsRequest {
        private String startDate;
        private String endDate;
        private Integer statusFilter;  // 状态过滤：null=全部, 0=待支付, 1=已支付, 2=已取消
        
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public Integer getStatusFilter() { return statusFilter; }
        public void setStatusFilter(Integer statusFilter) { this.statusFilter = statusFilter; }
    }
    
    /**
     * 订单统计响应对象
     */
    public static class OrderStatisticsResponse {
        private Integer totalOrders;
        private BigDecimal totalAmount;
        private BigDecimal avgOrderAmount;
        
        // 按状态统计
        private Integer pendingOrders;
        private Integer paidOrders;
        private Integer cancelledOrders;
        
        // 按金额区间统计
        private Integer smallOrders;   // 0-5000
        private Integer mediumOrders;  // 5000-20000
        private Integer largeOrders;   // 20000+
        
        // 趋势数据
        private int[] dailyOrderCounts;
        private double[] dailyAmounts;
        
        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public BigDecimal getAvgOrderAmount() { return avgOrderAmount; }
        public void setAvgOrderAmount(BigDecimal avgOrderAmount) { this.avgOrderAmount = avgOrderAmount; }
        
        public Integer getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Integer pendingOrders) { this.pendingOrders = pendingOrders; }
        public Integer getPaidOrders() { return paidOrders; }
        public void setPaidOrders(Integer paidOrders) { this.paidOrders = paidOrders; }
        public Integer getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(Integer cancelledOrders) { this.cancelledOrders = cancelledOrders; }
        
        public Integer getSmallOrders() { return smallOrders; }
        public void setSmallOrders(Integer smallOrders) { this.smallOrders = smallOrders; }
        public Integer getMediumOrders() { return mediumOrders; }
        public void setMediumOrders(Integer mediumOrders) { this.mediumOrders = mediumOrders; }
        public Integer getLargeOrders() { return largeOrders; }
        public void setLargeOrders(Integer largeOrders) { this.largeOrders = largeOrders; }
        
        public int[] getDailyOrderCounts() { return dailyOrderCounts; }
        public void setDailyOrderCounts(int[] dailyOrderCounts) { this.dailyOrderCounts = dailyOrderCounts; }
        public double[] getDailyAmounts() { return dailyAmounts; }
        public void setDailyAmounts(double[] dailyAmounts) { this.dailyAmounts = dailyAmounts; }
    }
    
    /**
     
     */
    @GetMapping("/{orderId}/detail-report")
    public OrderDetailReportResponse getOrderDetailReport(@PathVariable Long orderId) {
        // 1. 参数验证
        if (orderId == null || orderId <= 0) {
            throw new IllegalArgumentException("订单ID不能为空或无效");
        }
        
        // 2. 查询订单基础信息
        OrderDTO order = orderService.getOrderById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在，订单ID: " + orderId);
        }
        
        // 3. 构建详细报告
        OrderDetailReportResponse response = new OrderDetailReportResponse();
        
        // 基础信息
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setProductName(order.getProductName());
        response.setAmount(order.getAmount());
        response.setStatus(order.getStatus());
        response.setCreateTime(order.getCreateTime());
        
        // 状态描述
        String statusText;
        switch (order.getStatus()) {
            case 0:
                statusText = "待支付";
                break;
            case 1:
                statusText = "已支付";
                break;
            case 2:
                statusText = "已取消";
                break;
            default:
                statusText = "未知状态";
        }
        response.setStatusText(statusText);
        
        // 客户信息（模拟数据）
        response.setCustomerName("客户" + order.getUserId());
        response.setCustomerPhone("138****" + String.format("%04d", orderId % 10000));
        response.setCustomerEmail("customer" + order.getUserId() + "@example.com");
        
        // 支付信息（模拟数据）
        if (order.getStatus() == 1) {
            response.setPaymentMethod("支付宝");
            response.setPaymentTime("2025-01-15 10:30:00");
            response.setTransactionId("TXN" + orderId + System.currentTimeMillis());
        }
        
        // 物流信息（模拟数据）
        if (order.getStatus() == 1) {
            response.setShippingAddress("北京市朝阳区某某街道123号");
            response.setShippingStatus("已发货");
            response.setTrackingNumber("SF" + orderId + "2025");
        }
        
        // 订单备注
        response.setRemark("订单详细报告 - 订单ID: " + orderId);
        
        // 4. 记录日志
        System.out.println("订单详细报告查询 - 订单ID: " + orderId + 
                         ", 状态: " + statusText + 
                         ", 金额: " + order.getAmount());
        
        return response;
    }
    
    /**
     * 订单详细报告响应对象
     */
    public static class OrderDetailReportResponse {
        // 基础信息
        private Long orderId;
        private Long userId;
        private String productName;
        private BigDecimal amount;
        private Integer status;
        private String statusText;
        private String createTime;
        
        // 客户信息
        private String customerName;
        private String customerPhone;
        private String customerEmail;
        
        // 支付信息
        private String paymentMethod;
        private String paymentTime;
        private String transactionId;
        
        // 物流信息
        private String shippingAddress;
        private String shippingStatus;
        private String trackingNumber;
        
        // 备注
        private String remark;
        
        // Getters and Setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
        public String getStatusText() { return statusText; }
        public void setStatusText(String statusText) { this.statusText = statusText; }
        public String getCreateTime() { return createTime; }
        public void setCreateTime(String createTime) { this.createTime = createTime; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
        public String getCustomerEmail() { return customerEmail; }
        public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
        
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getPaymentTime() { return paymentTime; }
        public void setPaymentTime(String paymentTime) { this.paymentTime = paymentTime; }
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
        
        public String getShippingAddress() { return shippingAddress; }
        public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
        public String getShippingStatus() { return shippingStatus; }
        public void setShippingStatus(String shippingStatus) { this.shippingStatus = shippingStatus; }
        public String getTrackingNumber() { return trackingNumber; }
        public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
        
        public String getRemark() { return remark; }
        public void setRemark(String remark) { this.remark = remark; }
    }
}
