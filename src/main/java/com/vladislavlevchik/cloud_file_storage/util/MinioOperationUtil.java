package com.vladislavlevchik.cloud_file_storage.util;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class MinioOperationUtil {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public void put(String fileKey, InputStream stream, long size, String contentType) throws Exception{
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileKey)
                .stream(stream, size, -1)
                .contentType(contentType)
                .build());
    }

    public Iterable<Result<Item>> listObjects(String folderPrefix) throws Exception {
        return minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(folderPrefix)
                        .recursive(true)
                        .build()
        );
    }

    public void remove(String fileName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
    }

    public void copy(String sourceFileName, String targetFileName) throws Exception {
        minioClient.copyObject(CopyObjectArgs.builder()
                .bucket(bucketName)
                .object(targetFileName)
                .source(CopySource.builder()
                        .bucket(bucketName)
                        .object(sourceFileName)
                        .build())
                .build());
    }

}
