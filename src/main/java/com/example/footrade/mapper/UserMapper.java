package com.example.footrade.mapper;

import com.example.footrade.DTO.UserPreferenceDTO;
import com.example.footrade.DTO.UserRegisterDTO;
import com.example.footrade.DTO.UserResponseDTO;
import com.example.footrade.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UserMapper.class)
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);
    User fromUserRegisterDTO(UserRegisterDTO userRegisterDTO);
    UserPreferenceDTO toUserPreferenceDTO(User user);
    UserResponseDTO toUserResponseDTO(User user);
}
