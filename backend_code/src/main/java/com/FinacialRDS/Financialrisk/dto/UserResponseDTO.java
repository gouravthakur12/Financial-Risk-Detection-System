package com.FinacialRDS.Financialrisk.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String status;
}