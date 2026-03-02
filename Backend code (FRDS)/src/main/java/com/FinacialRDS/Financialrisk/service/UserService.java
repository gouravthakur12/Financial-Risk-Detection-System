package com.FinacialRDS.Financialrisk.service;

import com.FinacialRDS.Financialrisk.entity.User;
import com.FinacialRDS.Financialrisk.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.FinacialRDS.Financialrisk.dto.UserRequestDTO;
import com.FinacialRDS.Financialrisk.dto.UserResponseDTO;
import com.FinacialRDS.Financialrisk.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPasswordHash(passwordEncoder.encode(requestDTO.getPassword())); // hash with BCrypt
        user.setRole(requestDTO.getRole());
        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUserId(savedUser.getUserId());
        responseDTO.setName(savedUser.getName());
        responseDTO.setEmail(savedUser.getEmail());
        responseDTO.setRole(savedUser.getRole());
        responseDTO.setStatus(savedUser.getStatus());

        return responseDTO;
    }

    public boolean validateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return passwordEncoder.matches(password, user.getPasswordHash()); // BCrypt verification
    }
}