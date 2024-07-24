package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.UserRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserAlreadyExistsException;
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

    public void signIn(UserRequestDto userRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //TODO мб сделать одним запросом, через запрос к бд на сохранение
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        repository.findByUsername(userRequestDto.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("User with username " + userRequestDto.getUsername() + " already exists");
                });


        userRequestDto.setPassword(encoder.encode(userRequestDto.getPassword()));

        User user = repository.save(mapper.map(userRequestDto, User.class));

        return mapper.map(user, UserResponseDto.class);
    }

}
