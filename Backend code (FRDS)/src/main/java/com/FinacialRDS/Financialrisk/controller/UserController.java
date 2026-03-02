package com.FinacialRDS.Financialrisk.controller;
import jakarta.validation.Valid;
import com.FinacialRDS.Financialrisk.dto.UserRequestDTO;
import com.FinacialRDS.Financialrisk.dto.UserResponseDTO;
import com.FinacialRDS.Financialrisk.entity.User;
import com.FinacialRDS.Financialrisk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserResponseDTO> addUser(
        @Valid @RequestBody UserRequestDTO requestDTO) {
      UserResponseDTO response = userService.createUser(requestDTO);
    return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}