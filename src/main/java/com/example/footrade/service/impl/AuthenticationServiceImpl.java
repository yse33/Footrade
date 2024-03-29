package com.example.footrade.service.impl;

import com.example.footrade.DTO.UserLoginDTO;
import com.example.footrade.DTO.UserRegisterDTO;
import com.example.footrade.DTO.UserResponseDTO;
import com.example.footrade.entity.User;
import com.example.footrade.repository.UserRepository;
import com.example.footrade.security.JWTGenerator;
import com.example.footrade.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.footrade.mapper.UserMapper.USER_MAPPER;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    @Override
    public UserResponseDTO register(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsername(userRegisterDTO.getUsername())) {
            throw new DuplicateKeyException("Username already exists");
        }

        User user = USER_MAPPER.fromUserRegisterDTO(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        userRepository.save(user);

        UserResponseDTO userResponseDTO = USER_MAPPER.toUserResponseDTO(user);
        userResponseDTO.setToken(jwtGenerator.generateToken(user));
        userResponseDTO.setHasPreference(user.getPreference() != null);

        return userResponseDTO;
    }

    @Override
    public UserResponseDTO login(UserLoginDTO userLoginDTO) {
        User user;
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getUsername(),
                            userLoginDTO.getPassword()
                    )
            );
            user = (User) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            if (userRepository.existsByUsername(userLoginDTO.getUsername())) {
                throw new BadCredentialsException("Wrong password!", e);
            } else {
                throw new BadCredentialsException("Wrong username!", e);
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponseDTO userResponseDTO = USER_MAPPER.toUserResponseDTO(user);
        userResponseDTO.setToken(jwtGenerator.generateToken(user));
        userResponseDTO.setHasPreference(user.getPreference() != null);

        return userResponseDTO;
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
