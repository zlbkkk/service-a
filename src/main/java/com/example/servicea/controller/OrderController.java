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
     * 新增接口：返回订单的状态文本描述
     */
    @GetMapping("/{orderId}/status-text")
    public String getOrderStatusText(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
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
            default:
                return "未知状态";
        }
    }
}
