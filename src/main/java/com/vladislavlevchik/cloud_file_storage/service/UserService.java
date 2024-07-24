package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.UserRequestDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserAlreadyExistsException;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public User save(UserRequestDto userRequestDto) {
        repository.findByUsername(userRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with username " + userRequestDto.getUsername() + " already exists");
                });


        userRequestDto.setPassword(encoder.encode(userRequestDto.getPassword()));

        return repository.save(mapper.map(userRequestDto, User.class));
    }

}
