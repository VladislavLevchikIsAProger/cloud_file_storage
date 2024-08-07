package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.UserNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.CustomFolderRepository;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;
    private final CustomFolderRepository customFolderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Value("${minio.bucket.name}")
    private final String bucketName;

    private final static String USER_PACKAGE_PREFIX = "user-";
    private static final long MEGABYTE = 1_048_576; // 1024 * 1024
    private static final long KILOBYTE = 1_024; // 1024


    public void createFolder(String username, FolderRequestDto dto) {
        CustomFolder folder = mapper.map(dto, CustomFolder.class);

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        folder.setUser(user);

        customFolderRepository.save(folder);
    }

    @SneakyThrows
    public List<FolderResponseDto> getList(String username) {
        String folderPrefix = USER_PACKAGE_PREFIX + username;

        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User " + username + " not found")
                );

        List<CustomFolder> folders = user.getFolders();

        List<FolderResponseDto> responseDtos = new ArrayList<>();

        for (CustomFolder folder : folders) {
            Iterable<Result<Item>> results = recursivelyTraverseFolders(folderPrefix + "/" + folder.getName());

            int itemCount = 0;
            long totalSize = 0;

            for (Result<Item> result : results) {
                Item item = result.get();
                if (item.objectName().endsWith(".empty")) {
                    continue;
                }

                itemCount++;
                totalSize += item.size();
            }

            FolderResponseDto dto = FolderResponseDto.builder()
                    .name(folder.getName())
                    .color(folder.getColor())
                    .size(convertBytesToMbOrKb(totalSize))
                    .filesNumber(String.valueOf(itemCount))
                    .build();

            responseDtos.add(dto);
        }

        return responseDtos;
    }

    @SneakyThrows
    public void createSubFolder(String username, SubFolderRequestDto subFolderRequestDto) {
        String folderPath = USER_PACKAGE_PREFIX + username + "/"
                + subFolderRequestDto.getFolderPath() + "/" + subFolderRequestDto.getName() + "/";

        String emptyFilePath = folderPath + ".empty";

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(emptyFilePath)
                        .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                        .build()
        );
    }

    public String getFolderColor(String folderName, String username) {
        return customFolderRepository.findColorByNameAndUsername(folderName, username);
    }

    private Iterable<Result<Item>> recursivelyTraverseFolders(String folderPrefix) {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(folderPrefix)
                        .recursive(true)
                        .build()
        );
    }

    private String convertBytesToMbOrKb(long sizeInBytes) {
        String formattedSize;

        if (sizeInBytes >= MEGABYTE) {
            double sizeInMB = sizeInBytes / (double) MEGABYTE;
            formattedSize = String.format("%.2fMB", sizeInMB);
        } else {
            double sizeInKB = sizeInBytes / (double) KILOBYTE;
            formattedSize = String.format("%.2fKB", sizeInKB);
        }

        return formattedSize;
    }

}
