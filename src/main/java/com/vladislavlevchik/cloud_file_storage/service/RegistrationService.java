package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public void registerUser(UserRegisterRequestDto userRegisterRequestDto) {
        userRegisterRequestDto.setPassword(encoder.encode(userRegisterRequestDto.getPassword()));

        repository.save(mapper.map(userRegisterRequestDto, User.class));
    }
}
