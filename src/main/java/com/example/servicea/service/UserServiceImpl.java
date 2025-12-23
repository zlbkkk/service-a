package com.example.servicea.service;

import com.example.common.dto.UserDTO;
import com.example.common.service.UserService;
import com.example.servicea.client.NotificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

/**
 * 用户服务实现 - 实现 common-api 中的 UserService 接口
 * 并调用 service-b 的通知服务
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private NotificationClient notificationClient;

    @Override
    public UserDTO getUserById(Long id) {
        // 模拟从数据库获取用户
        return new UserDTO(id, "user" + id, "user" + id + "@example.com");
    }

    /**
     * 创建用户（Controller 调用的简化版本）
     */
    public UserDTO createUser(String username, String email) {
        return createUser(username, email, null);
    }
    
    @Override
    public UserDTO createUser(String username, String email, String phoneNumber) {
        // 验证邮箱
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        // 模拟创建用户
        UserDTO newUser = new UserDTO(System.currentTimeMillis(), username, email, phoneNumber, 1);
        
        // 跨项目调用: 创建用户后，调用 service-b 发送欢迎邮件
        try {
            String result = notificationClient.sendWelcomeEmail(newUser);
            System.out.println("Welcome email sent: " + result);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
        
        return newUser;
    }

    @Override
    public boolean validateEmail(String email) {
        // 使用 UserService 接口中定义的方法
        return email != null && email.contains("@");
    }
    
    public List<UserDTO> getAllUsers() {
        // 模拟获取所有用户
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO(1L, "alice", "alice@example.com"));
        users.add(new UserDTO(2L, "bob", "bob@example.com"));
        users.add(new UserDTO(3L, "charlie", "charlie@example.com"));
        return users;
    }
    
    public boolean updateUser(Long userId, UserDTO userDTO) {
        // 模拟更新用户信息
        UserDTO existingUser = getUserById(userId);
        if (existingUser != null) {
            // 跨项目调用: 用户信息变更时通知用户
            String message = "Your profile has been updated successfully";
            notificationClient.sendNotification(userDTO, message);
            return true;
        }
        return false;
    }
    
    public boolean deleteUser(Long userId) {
        // 模拟删除用户
        UserDTO user = getUserById(userId);
        if (user != null) {
            // 跨项目调用: 删除用户前发送通知
            String message = "Your account has been deleted";
            notificationClient.sendNotification(user, message);
            return true;
        }
        return false;
    }
    
    @Override
    public List<UserDTO> getUsersByIds(List<Long> ids) {
        // 模拟批量获取用户
        List<UserDTO> users = new ArrayList<>();
        for (Long id : ids) {
            users.add(getUserById(id));
        }
        return users;
    }
    
    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        // 模拟更新用户状态
        UserDTO user = getUserById(userId);
        if (user != null) {
            user.setStatus(status);
            
            // 跨项目调用: 状态变更时通知用户
            String message = "Your account status has been updated to: " + (status == 1 ? "Active" : "Inactive");
            notificationClient.sendNotification(user, message);
            
            return true;
        }
        return false;
    }
    
    @Override
    public UserDTO getUserByPhoneNumber(String phoneNumber) {
        // 模拟根据手机号查询用户
        return new UserDTO(1L, "phoneUser", "phone@example.com", phoneNumber, 1);
    }
}
