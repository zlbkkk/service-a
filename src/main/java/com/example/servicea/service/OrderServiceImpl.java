package com.example.servicea.service;

import com.example.common.constant.QueueConstant;
import com.example.common.dto.OrderDTO;
import com.example.common.dto.OrderEventDTO;
import com.example.common.dto.UserDTO;
import com.example.common.service.OrderService;
import com.example.servicea.client.NotificationClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现
 * 实现 common-api 中的 OrderService 接口
 * 并调用 service-b 发送订单通知
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private NotificationClient notificationClient;
    
    @Autowired
    private UserServiceImpl userService;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    @Value("${spring.rabbitmq.template.exchange:order.exchange}")
    private String exchangeName;

    @Override
    public OrderDTO createOrder(Long userId, String productName, BigDecimal amount) {
        // 创建订单
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        OrderDTO order = new OrderDTO(System.currentTimeMillis(), userId, orderNumber, amount);
        order.setProductName(productName);
        order.setStatus(0); // 待支付
        
        // 跨项目调用: 创建订单后，调用 service-b 发送订单确认通知
        try {
            UserDTO user = userService.getUserById(userId);
            String message = String.format(
                "Your order %s has been created successfully. Total amount: $%.2f",
                orderNumber, amount
            );
            notificationClient.sendNotification(user, message);
        } catch (Exception e) {
            System.err.println("Failed to send order notification: " + e.getMessage());
        }
        
        // RabbitMQ 消息发送: 发送订单创建事件到消息队列
        sendOrderEventAsync(order.getId(), orderNumber, userId, "CREATED", amount.doubleValue());
        
        return order;
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        // 模拟从数据库获取订单
        OrderDTO order = new OrderDTO(orderId, 1L, "ORD-12345", new BigDecimal("99.99"));
        // 新增：设置订单产品名称和状态
        order.setProductName("Premium Product");
        order.setStatus(1); // 1 = 已支付
        return order;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        // 模拟获取用户订单列表
        List<OrderDTO> orders = new ArrayList<>();
        orders.add(new OrderDTO(1L, userId, "ORD-12345", new BigDecimal("99.99")));
        orders.add(new OrderDTO(2L, userId, "ORD-67890", new BigDecimal("149.99")));
        return orders;
    }

    @Override
    public boolean updateOrderStatus(Long orderId, Integer status) {
        // 模拟更新订单状态
        OrderDTO order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            
            // 跨项目调用: 订单状态变更时通知用户
            try {
                UserDTO user = userService.getUserById(order.getUserId());
                String statusText = status == 1 ? "paid" : (status == 2 ? "cancelled" : "pending");
                String message = String.format(
                    "Your order %s status has been updated to: %s",
                    order.getOrderNumber(), statusText
                );
                notificationClient.sendNotification(user, message);
            } catch (Exception e) {
                System.err.println("Failed to send status update notification: " + e.getMessage());
            }
            
            // RabbitMQ 消息发送: 发送订单状态变更事件
            String eventType = status == 1 ? "PAID" : (status == 2 ? "CANCELLED" : "STATUS_UPDATED");
            sendOrderEventAsync(orderId, order.getOrderNumber(), order.getUserId(), eventType, order.getTotalAmount().doubleValue());
            
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, 2); // 2 = 已取消
    }
    
    /**
     * 获取订单状态文本描述
     * 这个方法会被 service-b 通过 Dubbo RPC 调用
     */
    @Override
    public String getOrderStatusText(Long orderId) {
        OrderDTO order = getOrderById(orderId);
        if (order == null) {
            return "订单不存在";
        }
        
        // 根据状态码返回中文描述
        switch (order.getStatus()) {
            case 0:
                return "待支付";
            case 1:
                return "已支付";
            case 2:
                return "已取消";
            case 3:
                return "已发货";
            case 4:
                return "已完成";
            default:
                return "未知状态";
        }
    }
    
    /**
     * 获取订单详细信息（包含状态文本）
     * 这个方法会被 service-b 通过 Dubbo RPC 调用
     */
    @Override
    public String getOrderDetails(Long orderId) {
        OrderDTO order = getOrderById(orderId);
        if (order == null) {
            return "订单不存在";
        }
        
        String statusText = getOrderStatusText(orderId);
        
        return String.format(
            "订单详情 - 订单号: %s, 用户ID: %d, 金额: ¥%.2f, 状态: %s",
            order.getOrderNumber(),
            order.getUserId(),
            order.getTotalAmount(),
            statusText
        );
    }
    
    /**
     * 获取订单摘要信息（用于测试 Dubbo RPC 调用）
     * 这个方法会被 service-b 通过 Dubbo RPC 调用
     * 
     * 修改说明：
     * 1. 增加订单产品信息
     * 2. 增加订单创建时间
     * 3. 增加订单支付时间（如果已支付）
     * 4. 返回更详细的格式化字符串
     */
    @Override
    public String getOrderSummary(Long orderId) {
        OrderDTO order = getOrderById(orderId);
        if (order == null) {
            return "订单不存在";
        }
        
        String statusText = getOrderStatusText(orderId);
        
        // 获取订单产品名称
        String productName = order.getProductName() != null ? order.getProductName() : "未知商品";
        
        // 模拟创建时间和支付时间
        String createTime = "2026-01-14 10:30:00";
        String payTime = order.getStatus() == 1 ? "2026-01-14 10:35:00" : "未支付";
        
        // 返回详细的摘要信息（新增了产品名称、创建时间、支付时间）
        return String.format(
            "订单号:%s|商品:%s|金额:¥%.2f|状态:%s|创建时间:%s|支付时间:%s",
            order.getOrderNumber(),
            productName,
            order.getTotalAmount(),
            statusText,
            createTime,
            payTime
        );
    }
    
    /**
     * 异步发送订单事件消息到 RabbitMQ
     * 模拟真实项目中的消息队列使用场景
     * 
     * @param orderId 订单ID
     * @param orderNo 订单编号
     * @param userId 用户ID
     * @param eventType 事件类型
     * @param amount 订单金额
     */
    private void sendOrderEventAsync(Long orderId, String orderNo, Long userId, String eventType, Double amount) {
        log.info("发送订单事件消息，订单编号：{}, 事件类型：{}", orderNo, eventType);
        
        OrderEventDTO eventDTO = OrderEventDTO.builder()
                .orderId(orderId)
                .orderNo(orderNo)
                .userId(userId)
                .orderStatus(eventType)
                .amount(amount)
                .eventType(eventType)
                .timestamp(System.currentTimeMillis())
                .build();
        
        // 发送消息到 RabbitMQ
        rabbitTemplate.convertAndSend(
                exchangeName, 
                QueueConstant.ORDER_EVENT_KEY, 
                eventDTO
        );
        
        log.info("订单事件消息发送成功，订单编号：{}, 事件类型：{}", orderNo, eventType);
    }
}
