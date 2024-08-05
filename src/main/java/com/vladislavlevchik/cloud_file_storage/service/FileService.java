package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.FileDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FileMoveRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FileNameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MemoryResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.TimeResponseDto;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
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
    private final static String USER_PACKAGE_PREFIX = "user-";
    private static final long MEGABYTE = 1_048_576; // 1024 * 1024
    private static final long KILOBYTE = 1_024; // 1024

    //TODO тоже с ексепшенами поработать
    @SneakyThrows
    public MemoryResponseDto getMemoryInfo(String username) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";

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

    //TODO потом надо нормально работу с ошибками обработать
    //TODO мб у нас не будет загрузки сразу в папку, поэтому мб придется без path делать
    @SneakyThrows
    public void uploadFile(String username, String path, List<MultipartFile> files) {
        if (path == null || path.trim().isEmpty()) {
            path = "";
        }

        for (MultipartFile file : files) {
            String fileKey = USER_PACKAGE_PREFIX + username + path + "/" + file.getOriginalFilename();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileKey)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        }
    }

    @SneakyThrows
    public List<FileResponseDto> listFilesInAllFiles(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";

        Iterable<Result<Item>> objects = recursivelyTraverseFolders(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            String formattedSize = convertBytesToMbOrKb(item.size());

            if (!objectName.startsWith(USER_PACKAGE_PREFIX + username + "/deleted")) {
                fileList.add(createFileResponseDto(objectName, formattedSize, item));
            }

        }

        return fileList;
    }

    @SneakyThrows
    public List<FileResponseDto> listFilesInDeleted(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/deleted";

        Iterable<Result<Item>> objects = recursivelyTraverseFolders(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            String formattedSize = convertBytesToMbOrKb(item.size());

            fileList.add(createFileResponseDto(objectName, formattedSize, item));
        }

        return fileList;
    }

    @SneakyThrows
    public FileAndFolderResponseDto listFilesAndDirectories(String username, String folderPath) {
        List<String> folders = new ArrayList<>();
        List<FileResponseDto> files = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/" + folderPath;

        Iterable<Result<Item>> objects = recursivelyTraverseFolders(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            String formattedSize = convertBytesToMbOrKb(item.size());

            String relativePath = objectName.substring(folderPrefix.length() + 1);

            if (relativePath.contains("/")) {
                int index = relativePath.indexOf('/');
                String subPath = relativePath.substring(0, index);

                if (!folders.contains(subPath)) {
                    folders.add(subPath);
                }

            } else {
                files.add(FileResponseDto.builder()
                        .filename(relativePath)
                        .filePath(folderPath + "/")
                        .size(formattedSize)
                        .lastModified(createTimeResponseDto(item.lastModified()))
                        .build());
            }

        }

        return FileAndFolderResponseDto.builder()
                .folders(folders)
                .files(files)
                .build();
    }

    @SneakyThrows
    public void deleteFiles(String username, List<FileDeleteRequestDto> files) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + "/deleted";

        List<String> paths = new ArrayList<>();

        for (FileDeleteRequestDto file : files) {
            paths.add(folderPrefix + "/" + file.getFilePath() + file.getFilename());
        }

        Iterable<Result<Item>> objects = recursivelyTraverseFolders(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (paths.contains(fileName)) {
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
            }
        }
    }

    @SneakyThrows
    public void moveFiles(String username, FileMoveRequestDto fileMoveRequestDto) {
        String sourcePath = (fileMoveRequestDto.getSource().isEmpty())
                ? USER_PACKAGE_PREFIX + username
                : USER_PACKAGE_PREFIX + username + "/" + fileMoveRequestDto.getSource();

        String targetPath = USER_PACKAGE_PREFIX + username + "/" + fileMoveRequestDto.getTarget() + "/";

        List<String> pathsFiles = new ArrayList<>();

        for (FileNameRequestDto file : fileMoveRequestDto.getFiles()) {
            pathsFiles.add(sourcePath + "/" + file.getFilename());
        }

        Iterable<Result<Item>> objects = recursivelyTraverseFolders(sourcePath);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (pathsFiles.contains(fileName)) {
                String targetFilePath = targetPath + getFileName(fileName);

                minioClient.copyObject(CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(targetFilePath)
                        .source(CopySource.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .build())
                        .build());

                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build());
            }
        }
    }

    private FileResponseDto createFileResponseDto(String objectName, String formattedSize, Item item) {
        return FileResponseDto.builder()
                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                .filePath(getSubdirectories(objectName))
                .size(formattedSize)
                .lastModified(createTimeResponseDto(item.lastModified()))
                .build();
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

    /**
     * Extracts the base directory from a full path by removing the initial part and retaining only the directory.
     *
     * @param fullPath The full path to the file or directory, e.g., "user-Vlad/files/png/award-yel.png".
     * @return The base directory, e.g., "files/png/".
     */
    private String getSubdirectories(String fullPath) {
        fullPath = fullPath.replaceFirst("^[^/]+/", "");

        if (fullPath.startsWith("deleted/")) {
            fullPath = fullPath.substring("deleted/".length());
        }

        int lastSlashIndex = fullPath.lastIndexOf('/');
        return (lastSlashIndex == -1) ? "" : fullPath.substring(0, lastSlashIndex + 1);
    }

    private String getFileName(String filePath) {
        int lastSlashIndex = filePath.lastIndexOf('/');
        return (lastSlashIndex == -1) ? filePath : filePath.substring(lastSlashIndex + 1);
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

    private TimeResponseDto createTimeResponseDto(ZonedDateTime time) {
        return TimeResponseDto.builder()
                .day(time.toLocalDate().toString())
                .time(time.toLocalTime().toString())
                .build();
    }
}
