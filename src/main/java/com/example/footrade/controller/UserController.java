package com.example.footrade.controller;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.enums.Brand;
import com.example.footrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/preference")
    public ResponseEntity<UserPreferenceDTO> setPreference(
            @RequestParam List<String> brands,
            @RequestParam List<String> sizes
    ){
        return ResponseEntity.ok(userService.setPreference(
                new Preference(brands.stream().map(Brand::valueOf).toList(), sizes)
        ));
    }

    @PutMapping("/favorite")
    public ResponseEntity<Void> setFavorite(
            @RequestParam String id
    ){
        userService.setFavorite(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/device-token")
    public ResponseEntity<Void> setDeviceToken(
            @RequestParam String deviceToken
    ){
        userService.setDeviceToken(deviceToken);
        return ResponseEntity.ok().build();
    }
}
