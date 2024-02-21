package com.example.footrade.service;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.DTO.UserResponseDTO;
import com.example.footrade.DTO.UserLoginDTO;
import com.example.footrade.DTO.UserRegisterDTO;
import com.example.footrade.entity.Preference;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO userRegisterDTO);
    UserResponseDTO login(UserLoginDTO userLoginDTO);
    UserPreferenceDTO setPreference(String username, Preference preference);
    void setFavorite(String username, String blogId);
    void setDeviceToken(String username, String deviceToken);
    List<String> getDeviceTokensByShoeIds(List<ObjectId> shoeIds);
}
