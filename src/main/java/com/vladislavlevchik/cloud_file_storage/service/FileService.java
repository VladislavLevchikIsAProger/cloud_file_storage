package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MemoryResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.TimeResponseDto;
import com.vladislavlevchik.cloud_file_storage.util.MinioOperationUtil;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;
    private final MinioOperationUtil minio;

    @Value("${minio.bucket.name}")
    private final String bucketName;

    @Value("${minio.user.memory}")
    private final int userMemory;

    private final FolderService folderService;

    private final static String USER_PACKAGE_PREFIX = "user-";
    private static final long MEGABYTE = 1_048_576; // 1024 * 1024
    private static final long KILOBYTE = 1_024; // 1024

    //TODO тоже с ексепшенами поработать
    @SneakyThrows
    public MemoryResponseDto getMemoryInfo(String username) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";

        long totalSize = 0;

        Iterable<Result<Item>> results = minio.listObjects(folderPrefix);

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
            String fileKey = (path.isEmpty())
                    ? USER_PACKAGE_PREFIX + username + path + "/" + file.getOriginalFilename()
                    : USER_PACKAGE_PREFIX + username + "/" + path + "/" + file.getOriginalFilename();

            minio.put(fileKey, file.getInputStream(), file.getSize(), file.getContentType());
        }
    }

    @SneakyThrows
    public List<FileResponseDto> listFilesInAllFiles(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }

            String formattedSize = convertBytesToMbOrKb(item.size());

            if (!objectName.startsWith(USER_PACKAGE_PREFIX + username + "/deleted")) {
                String mainSubdirectory = getMainSubdirectory(objectName);

                String folderColor = folderService.getFolderColor(mainSubdirectory, username);

                fileList.add(
                        FileResponseDto.builder()
                                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                                .filePath(getSubdirectories(objectName))
                                .size(formattedSize)
                                .lastModified(createTimeResponseDto(item.lastModified()))
                                .customFolderName(mainSubdirectory)
                                .color(folderColor)
                                .build()
                );
            }

        }

        return fileList;
    }

    @SneakyThrows
    public List<FileResponseDto> listFilesInDeleted(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/deleted";

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            String formattedSize = convertBytesToMbOrKb(item.size());

            fileList.add(
                    FileResponseDto.builder()
                            .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                            .filePath(getSubdirectories(objectName))
                            .size(formattedSize)
                            .lastModified(createTimeResponseDto(item.lastModified()))
                            .build()
            );
        }

        return fileList;
    }

    @SneakyThrows
    public FileAndFolderResponseDto listFilesAndDirectories(String username, String folderPath) {
        List<String> folders = new ArrayList<>();
        List<FileResponseDto> files = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/" + folderPath;

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }

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
                        .filePath(folderPath)
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
            String filePath = (file.getFilePath().isEmpty())
                    ? USER_PACKAGE_PREFIX + username + "/deleted/" + file.getFilename()
                    : USER_PACKAGE_PREFIX + username + "/deleted/" + file.getFilePath() + "/" + file.getFilename();
            paths.add(filePath);
        }

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (paths.contains(fileName)) {
                minio.remove(fileName);
            }
        }
    }

    @SneakyThrows
    public void moveFiles(String username, FileMoveRequestDto files) {
        processFiles(username, files.getSource(), files.getTarget(), files.getFiles(), true);
    }

    @SneakyThrows
    public void copyFiles(String username, FileCopyRequestDto files) {
        processFiles(username, files.getSource(), files.getTarget(), files.getFiles(), false);
    }

    @SneakyThrows
    public void renameFile(String username, FileRenameRequestDto fileRenameRequestDto) {
        String sourcePath = (fileRenameRequestDto.getFilepath().isEmpty())
                ? USER_PACKAGE_PREFIX + username
                : USER_PACKAGE_PREFIX + username + "/" + fileRenameRequestDto.getFilepath();

        String pathOldFile = sourcePath + "/" + fileRenameRequestDto.getOldFileName();
        String pathNewFile = sourcePath + "/" + fileRenameRequestDto.getNewFileName();

        Iterable<Result<Item>> objects = minio.listObjects(sourcePath);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (fileName.equals(pathOldFile)) {
                minio.copy(fileName, pathNewFile);

                minio.remove(fileName);
            }

        }

    }

    @SneakyThrows
    public void recoverFiles(String username, List<FileRecoverRequestDto> files) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + "/deleted";

        Map<String, String> pathsMap = new HashMap<>();

        for (FileRecoverRequestDto file : files) {
            String sourcePath = (file.getFilePath().isEmpty())
                    ? folderPrefix + "/" + file.getFilename()
                    : folderPrefix + "/" + file.getFilePath() + "/" + file.getFilename();

            String destinationPath = (file.getFilePath().isEmpty())
                    ? USER_PACKAGE_PREFIX + username + "/" + file.getFilename()
                    : USER_PACKAGE_PREFIX + username + "/" + file.getFilePath() + "/" + file.getFilename();

            pathsMap.put(sourcePath, destinationPath);
        }

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (pathsMap.containsKey(fileName)) {
                String destinationPath = pathsMap.get(fileName);

                minio.copy(fileName, destinationPath);

                minio.remove(fileName);
            }
        }
    }

    @SneakyThrows
    private void processFiles(String username, String source, String target, List<FileNameRequestDto> files, boolean isMoveOperation) {
        String sourcePath = (source.isEmpty())
                ? USER_PACKAGE_PREFIX + username
                : USER_PACKAGE_PREFIX + username + "/" + source;

        String targetPath = USER_PACKAGE_PREFIX + username + "/" + target + "/";

        List<String> pathsFiles = files.stream()
                .map(file -> sourcePath + "/" + file.getFilename())
                .toList();

        Iterable<Result<Item>> objects = minio.listObjects(sourcePath);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (pathsFiles.contains(fileName)) {
                String targetFilePath = targetPath + getFileName(fileName);

                minio.copy(fileName, targetFilePath);

                if (isMoveOperation) {
                    minio.remove(fileName);
                }
            }
        }
    }

    /**
     * Extracts the base directory from a full path by removing the initial part and retaining only the directory.
     *
     * @param fullPath The full path to the file or directory, e.g., "user-Vlad/files/png/award-yel.png".
     * @return The base directory, e.g., "files/png".
     */
    private String getSubdirectories(String fullPath) {
        fullPath = fullPath.replaceFirst("^[^/]+/", "");

        if (fullPath.startsWith("deleted/")) {
            fullPath = fullPath.substring("deleted/".length());
        }

        int lastSlashIndex = fullPath.lastIndexOf('/');

        return (lastSlashIndex == -1) ? "" : fullPath.substring(0, lastSlashIndex);
    }

    private String getMainSubdirectory(String path) {
        String strippedPath = path.substring(path.indexOf('/') + 1);

        int nextSlashIndex = strippedPath.indexOf('/');

        if (nextSlashIndex != -1) {
            return strippedPath.substring(0, nextSlashIndex);
        } else {
            return "";
        }
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
