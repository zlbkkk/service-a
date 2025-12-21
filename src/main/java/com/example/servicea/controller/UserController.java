package com.example.servicea.controller;

import com.example.common.dto.UserDTO;
import com.example.common.service.UserService;
import com.example.servicea.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器 - 使用 common-api 中的 UserDTO 和 UserService
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@RequestParam String username, @RequestParam String email) {
        return userService.createUser(username, email);
    }
}
