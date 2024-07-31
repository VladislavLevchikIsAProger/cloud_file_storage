package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.MemoryResponseDto;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Service
public class FileService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final int userMemory;

    public FileService(MinioClient minioClient,
                       @Value("${minio.bucket.name}") String bucketName,
                       @Value("${minio.user.memory}") int userMemory) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        this.userMemory = userMemory;
    }

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

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(folderPrefix)
                        .recursive(true)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            totalSize += item.size();
        }

        return MemoryResponseDto.builder()
                .totalSize(valueOf(totalSize / (1024.0)).setScale(2, HALF_UP))
                .userMemory(userMemory)
                .build();
    }
}
