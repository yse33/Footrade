package com.example.footrade.controller;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.DTO.UserResponseDTO;
import com.example.footrade.DTO.UserLoginDTO;
import com.example.footrade.DTO.UserRegisterDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.enums.Brand;
import com.example.footrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return ResponseEntity.ok(userService.register(userRegisterDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.login(userLoginDTO));
    }

    @PutMapping("/preference")
    public ResponseEntity<UserPreferenceDTO> setPreference(
            @RequestParam String username,
            @RequestParam List<String> brands,
            @RequestParam List<String> sizes
    ){
        return ResponseEntity.ok(userService.setPreference(
                username,
                new Preference(brands.stream().map(Brand::valueOf).toList(), sizes)
        ));
    }

    @PutMapping("/favorite")
    public ResponseEntity<Void> setFavorite(
            @RequestParam String username,
            @RequestParam String id
    ){
        userService.setFavorite(username, id);
        return ResponseEntity.ok().build();
    }
}
