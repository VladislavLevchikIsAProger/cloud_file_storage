package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.CustomFolderRepository;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final CustomFolderRepository customFolderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public void createFolder(FolderRequestDto dto) {
        CustomFolder folder = mapper.map(dto, CustomFolder.class);

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(
                        () -> new UserNotFoundException("User " + dto.getUsername() + " not found")
                );

        folder.setUser(user);

        customFolderRepository.save(folder);
    }

    public List<FolderResponseDto> getList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        List<CustomFolder> folders = user.getFolders();

        return folders.stream()
                .map(folder -> mapper.map(folder, FolderResponseDto.class))
                .toList();
    }

}
