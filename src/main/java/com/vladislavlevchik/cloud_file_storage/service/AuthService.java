package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public void signIn(UserLoginRequestDto userLoginRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserResponseDto registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        userRegisterRequestDto.setPassword(encoder.encode(userRegisterRequestDto.getPassword()));

        User user = repository.save(mapper.map(userRegisterRequestDto, User.class));

        return mapper.map(user, UserResponseDto.class);
    }

}
