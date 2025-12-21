package com.example.servicea.service;

import com.example.common.dto.UserDTO;
import com.example.common.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现 - 实现 common-api 中的 UserService 接口
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserDTO getUserById(Long id) {
        // 模拟从数据库获取用户
        return new UserDTO(id, "user" + id, "user" + id + "@example.com");
    }

    @Override
    public UserDTO createUser(String username, String email) {
        // 验证邮箱
        if (!validateEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // 模拟创建用户
        return new UserDTO(System.currentTimeMillis(), username, email);
    }

    @Override
    public boolean validateEmail(String email) {
        // 使用 UserService 接口中定义的方法
        return email != null && email.contains("@");
    }
}
