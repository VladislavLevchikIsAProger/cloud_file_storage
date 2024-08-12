package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.user.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    public List<CustomFolder> getListFolders(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        return user.getFolders();
    }

    public User getUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );
    }

    public void register(UserRegisterRequestDto userRegisterRequestDto) {
        userRegisterRequestDto.setPassword(encoder.encode(userRegisterRequestDto.getPassword()));

        userRepository.save(mapper.map(userRegisterRequestDto, User.class));
    }
}
