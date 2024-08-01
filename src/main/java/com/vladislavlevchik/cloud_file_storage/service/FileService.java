package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.MemoryResponseDto;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    @Value("${minio.bucket.name}")
    private final String bucketName;
    @Value("${minio.user.memory}")
    private final int userMemory;

    //TODO потом надо нормально работу с ошибками обработать
    @SneakyThrows
    public void uploadFile(String username, String path, MultipartFile file) {
        String fileKey = "user-" + username + "/" + path + "/" + file.getOriginalFilename();

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileKey)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
    }

    //TODO тоже с ексепшенами поработать
    @SneakyThrows
    public MemoryResponseDto getMemoryInfo(String username) {
        String folderPrefix = "user-" + username + "/";

        long totalSize = 0;

        Iterable<Result<Item>> results = recursivelyTraverseFolders(folderPrefix);

        for (Result<Item> result : results) {
            Item item = result.get();
            totalSize += item.size();
        }

        return MemoryResponseDto.builder()
                .totalSize(valueOf(totalSize / (1024.0 * 1000)).setScale(2, HALF_UP))
                .userMemory(userMemory)
                .build();
    }

    @SneakyThrows
    public List<String> getAllFiles(String username) {
        String folderPrefix = "user-" + username + "/";

        Iterable<Result<Item>> results = recursivelyTraverseFolders(folderPrefix);

        List<String> files = new ArrayList<>();

        for (Result<Item> result : results) {
            Item item = result.get();
            String fileName = item.objectName();
            files.add(fileName.substring(fileName.lastIndexOf('/') + 1));
        }

        return files;
    }

    @SneakyThrows
    public void moveToPackage(String sourceObject, String targetObject) {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucketName)
                .object(targetObject)
                .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(sourceObject)
                        .build())
                .build());

        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(sourceObject)
                .build());
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
}
