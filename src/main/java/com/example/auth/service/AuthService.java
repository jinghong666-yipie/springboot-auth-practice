package com.example.auth.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.entity.User;
import com.example.auth.exception.AuthException;
import com.example.auth.mapper.UserMapper;
import com.example.auth.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userMapper.selectOne(
                Wrappers.<User>lambdaQuery().eq(User::getUsername, request.getUsername()));

        // 统一提示，避免泄露用户名是否存在
        if (user == null || user.getStatus() == null || user.getStatus() != 1
                || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("用户名或密码错误");
        }

        String token = jwtUtil.generate(user.getId(), user.getUsername());
        return new LoginResponse(token, "Bearer", 3600);
    }
}
