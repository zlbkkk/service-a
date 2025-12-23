package com.example.servicea.controller;

import com.example.common.dto.UserDTO;
import com.example.servicea.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 提供用户相关的 REST API
 * 这些接口会被 service-b 调用
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 创建用户
     * 被 service-b 的 UserClient.createUser() 调用
     */
    @PostMapping
    public UserDTO createUser(
            @RequestParam String username,
            @RequestParam String email) {
        return userService.createUser(username, email);
    }

    /**
     * 获取用户信息
     * 被 service-b 的 UserClient.getUserById() 调用
     */
    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    /**
     * 获取所有用户
     */
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public boolean updateUser(
            @PathVariable Long userId,
            @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}
