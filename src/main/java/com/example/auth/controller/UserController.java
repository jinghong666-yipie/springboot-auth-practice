package com.example.auth.controller;

import com.example.auth.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(HttpServletRequest request) {
        return ApiResponse.ok(Map.of(
                "userId", request.getAttribute("userId"),
                "username", request.getAttribute("username")));
    }
}
