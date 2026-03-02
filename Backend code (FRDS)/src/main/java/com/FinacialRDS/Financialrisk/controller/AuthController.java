package com.FinacialRDS.Financialrisk.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.FinacialRDS.Financialrisk.security.JwtUtil;
import com.FinacialRDS.Financialrisk.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        boolean valid = userService.validateUser(request.getEmail(), request.getPassword());

        if (!valid) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(token);
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}