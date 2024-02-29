package com.example.footrade.service.impl;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.entity.Preference;
import com.example.footrade.entity.User;
import com.example.footrade.repository.UserRepository;
import com.example.footrade.service.AuthenticationService;
import com.example.footrade.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.footrade.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Override
    public UserPreferenceDTO setPreference(Preference preference) {
        User user = authenticationService.getCurrentUser();
        user.setPreference(preference);
        userRepository.save(user);
        return USER_MAPPER.toUserPreferenceDTO(user);
    }

    @Override
    public void setFavorite(String id) {
        User user = authenticationService.getCurrentUser();

        if (user.getFavorites().contains(new ObjectId(id))) {
            user.getFavorites().remove(new ObjectId(id));
        } else {
            user.getFavorites().add(new ObjectId(id));
        }

        userRepository.save(user);
    }

    @Override
    public void setDeviceToken(String deviceToken) {
        User user = authenticationService.getCurrentUser();
        user.setDeviceToken(deviceToken);
        userRepository.save(user);
    }

    @Override
    public List<String> getDeviceTokensByShoeIds(List<ObjectId> shoeIds) {
        return userRepository.findByFavoritesIn(shoeIds).stream()
                .map(User::getDeviceToken)
                .toList();
    }

}
