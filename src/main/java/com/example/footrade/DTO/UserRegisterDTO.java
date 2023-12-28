package com.example.footrade.DTO;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserRegisterDTO {
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;
}
