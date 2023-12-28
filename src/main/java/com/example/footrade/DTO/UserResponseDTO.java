package com.example.footrade.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String token;
    private String tokenType = "Bearer ";
}
