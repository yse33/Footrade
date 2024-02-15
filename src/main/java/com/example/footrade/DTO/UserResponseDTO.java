package com.example.footrade.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private Boolean hasPreference;
    private String token;
    private String tokenType = "Bearer ";
}
