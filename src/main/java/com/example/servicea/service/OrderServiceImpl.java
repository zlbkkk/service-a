package com.example.servicea.service;

import com.example.common.dto.OrderDTO;
import com.example.common.dto.UserDTO;
import com.example.common.service.OrderService;
import com.example.servicea.client.NotificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现
 * 实现 common-api 中的 OrderService 接口
 * 并调用 service-b 发送订单通知
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private NotificationClient notificationClient;
    
    @Autowired
    private UserServiceImpl userService;

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
        
        return order;
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        // 模拟从数据库获取订单
        return new OrderDTO(orderId, 1L, "ORD-12345", new BigDecimal("99.99"));
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
            
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, 2); // 2 = 已取消
    }
}
