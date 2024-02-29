package com.example.footrade.service;

import com.example.footrade.DTO.UserLoginDTO;
import com.example.footrade.DTO.UserRegisterDTO;
import com.example.footrade.DTO.UserResponseDTO;
import com.example.footrade.entity.User;

public interface AuthenticationService {
    UserResponseDTO register(UserRegisterDTO userRegisterDTO);
    UserResponseDTO login(UserLoginDTO userLoginDTO);
    User getCurrentUser();
}
