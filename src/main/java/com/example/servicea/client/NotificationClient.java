package com.example.servicea.client;

import com.example.common.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 通知服务客户端 - 调用 service-b 的 API
 * 这是一个跨项目的 API 调用示例
 */
@Component
public class NotificationClient {
    
    private final RestTemplate restTemplate;
    private static final String SERVICE_B_URL = "http://localhost:8082/api/notifications";
    
    public NotificationClient() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * 调用 service-b 的发送欢迎邮件接口
     * 跨项目调用: service-a -> service-b
     */
    public String sendWelcomeEmail(UserDTO user) {
        String url = SERVICE_B_URL + "/welcome";
        try {
            return restTemplate.postForObject(url, user, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
            return "Failed to send welcome email";
        }
    }
    
    /**
     * 调用 service-b 的发送通知接口
     * 跨项目调用: service-a -> service-b
     */
    public String sendNotification(UserDTO user, String message) {
        String url = SERVICE_B_URL + "/send?message=" + message;
        try {
            return restTemplate.postForObject(url, user, String.class);
        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
            return "Failed to send notification";
        }
    }
}
