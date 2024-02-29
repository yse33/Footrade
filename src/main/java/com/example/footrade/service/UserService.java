package com.example.footrade.service;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.entity.Preference;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {
    UserPreferenceDTO setPreference(Preference preference);
    void setFavorite(String blogId);
    void setDeviceToken(String deviceToken);
    List<String> getDeviceTokensByShoeIds(List<ObjectId> shoeIds);
}
