package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.*;
import com.vladislavlevchik.cloud_file_storage.util.StringUtil;
import com.vladislavlevchik.cloud_file_storage.util.MinioOperationUtil;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.ZoneId;
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

    private final MinioOperationUtil minio;

    @Value("${minio.user.memory}")
    private final int userMemory;

    private final FolderService folderService;

    private final StringUtil stringUtil;

    private final static String USER_PACKAGE_PREFIX = "user-";
    private final static String DELETED_FOLDER = "/deleted/";

    @SneakyThrows
    public MemoryResponseDto getMemoryInfo(String username) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";
        long totalSize = 0;

        for (Result<Item> result : minio.listObjects(folderPrefix)) {
            Item item = result.get();
            totalSize += item.size();
        }

        return MemoryResponseDto.builder()
                .totalSize(valueOf(totalSize / (1024.0 * 1000)).setScale(2, HALF_UP))
                .userMemory(userMemory)
                .build();
    }

    @SneakyThrows
    public void uploadFile(String username, String path, List<MultipartFile> files) {
        String basePath = USER_PACKAGE_PREFIX + username + (path.isEmpty() ? "" : "/" + path);

        for (MultipartFile file : files) {
            String fileKey = basePath + "/" + file.getOriginalFilename();
            minio.put(fileKey, file.getInputStream(), file.getSize(), file.getContentType());
        }
    }

    @SneakyThrows
    public List<FileResponseDto> searchFile(String username, String fileName) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/";

        String lowerCaseFileName = fileName.toLowerCase();

        for (Result<Item> itemResult : minio.listObjects(folderPrefix)) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }

            String lowerCaseObjectName = stringUtil.getFileName(objectName).toLowerCase();

            if (lowerCaseObjectName.contains(lowerCaseFileName) && !objectName.startsWith(USER_PACKAGE_PREFIX + username + DELETED_FOLDER)) {
                String formattedSize = stringUtil.convertBytesToMbOrKb(item.size());

                String mainSubdirectory = stringUtil.getMainSubdirectory(objectName);

                String folderColor = folderService.getFolderColor(mainSubdirectory, username);

                fileList.add(
                        FileResponseDto.builder()
                                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                                .filePath(stringUtil.getSubdirectories(objectName))
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
    public List<FileResponseDto> searchFileInDeleted(String username, String fileName) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + DELETED_FOLDER;

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        String lowerCaseFileName = fileName.toLowerCase();

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }

            String lowerCaseObjectName = stringUtil.getFileName(objectName).toLowerCase();

            if (lowerCaseObjectName.contains(lowerCaseFileName)) {
                String formattedSize = stringUtil.convertBytesToMbOrKb(item.size());

                fileList.add(
                        FileResponseDto.builder()
                                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                                .filePath(stringUtil.getSubdirectories(objectName))
                                .size(formattedSize)
                                .lastModified(createTimeResponseDto(item.lastModified()))
                                .build()
                );
            }
        }

        return fileList;
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

            String formattedSize = stringUtil.convertBytesToMbOrKb(item.size());

            if (!objectName.startsWith(USER_PACKAGE_PREFIX + username + DELETED_FOLDER)) {
                String mainSubdirectory = stringUtil.getMainSubdirectory(objectName);

                String folderColor = folderService.getFolderColor(mainSubdirectory, username);

                fileList.add(
                        FileResponseDto.builder()
                                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                                .filePath(stringUtil.getSubdirectories(objectName))
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

        String folderPrefix = USER_PACKAGE_PREFIX + username + DELETED_FOLDER;

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            String formattedSize = stringUtil.convertBytesToMbOrKb(item.size());

            fileList.add(
                    FileResponseDto.builder()
                            .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                            .filePath(stringUtil.getSubdirectories(objectName))
                            .size(formattedSize)
                            .lastModified(createTimeResponseDto(item.lastModified()))
                            .build()
            );
        }

        return fileList;
    }

    @SneakyThrows
    public FileAndFolderResponseDto listFilesAndDirectories(String username, String folderPath) {
        Map<String, Long> folderSizes = new HashMap<>();
        Map<String, ZonedDateTime> folderLastModified = new HashMap<>();

        List<FileResponseDto> files = new ArrayList<>();
        List<SubFolderSizeResponseDto> folders = new ArrayList<>();

        String folderPrefix = USER_PACKAGE_PREFIX + username + "/" + folderPath + "/";

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String objectName = item.objectName();
            String relativePath = objectName.substring(folderPrefix.length());

            if (objectName.endsWith(".empty")) {
                int index = relativePath.indexOf('/');

                if (index > 0) {
                    String subPath = relativePath.substring(0, index);

                    folderSizes.putIfAbsent(subPath, 0L);
                    folderLastModified.putIfAbsent(subPath, item.lastModified());
                }
                continue;
            }

            String formattedSize = stringUtil.convertBytesToMbOrKb(item.size());

            if (relativePath.contains("/")) {
                int index = relativePath.indexOf('/');
                String subPath = relativePath.substring(0, index);

                folderSizes.put(subPath, folderSizes.getOrDefault(subPath, 0L) + item.size());

                ZonedDateTime currentLastModified = folderLastModified.getOrDefault(subPath, ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()));

                folderLastModified.put(subPath, currentLastModified.isAfter(item.lastModified()) ? currentLastModified : item.lastModified());
            } else {
                files.add(FileResponseDto.builder()
                        .filename(relativePath)
                        .filePath(folderPath)
                        .size(formattedSize)
                        .lastModified(createTimeResponseDto(item.lastModified()))
                        .build());
            }
        }

        for (Map.Entry<String, Long> entry : folderSizes.entrySet()) {
            String folderName = entry.getKey();
            String folderSize = stringUtil.convertBytesToMbOrKb(entry.getValue());
            ZonedDateTime lastModified = folderLastModified.get(folderName);
            folders.add(SubFolderSizeResponseDto.builder()
                    .name(folderName)
                    .size(folderSize)
                    .lastModified(createTimeResponseDto(lastModified))
                    .build());
        }

        return FileAndFolderResponseDto.builder()
                .folders(folders)
                .files(files)
                .build();
    }

    @SneakyThrows
    public void deleteFiles(String username, List<FileDeleteRequestDto> files) {
        String folderPrefix = USER_PACKAGE_PREFIX + username + DELETED_FOLDER;

        List<String> paths = new ArrayList<>();

        for (FileDeleteRequestDto file : files) {
            String filePath = (file.getFilePath().isEmpty())
                    ? folderPrefix + file.getFilename()
                    : folderPrefix + file.getFilePath() + "/" + file.getFilename();
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
    public void moveFilesToDeleted(String username, List<FileMoveToDeletedRequestDto> files) {
        for (FileMoveToDeletedRequestDto file : files) {
            String sourcePath = (file.getFilePath().isEmpty())
                    ? USER_PACKAGE_PREFIX + username
                    : USER_PACKAGE_PREFIX + username + "/" + file.getFilePath();

            String pathOldFile = sourcePath + "/" + file.getFilename();
            String pathNewFile = sourcePath + "/deleted/" + file.getFilePath() + "/" ;

            Iterable<Result<Item>> objects = minio.listObjects(pathOldFile);

            for (Result<Item> itemResult : objects) {
                Item item = itemResult.get();
                String fileName = item.objectName();

                if (fileName.equals(pathOldFile)) {
                    minio.copy(fileName, pathNewFile);

                    minio.remove(fileName);
                }

            }
        }
    }

    @SneakyThrows
    public void copyFiles(String username, FileCopyRequestDto files) {
        processFiles(username, files.getSource(), files.getTarget(), files.getFiles(), false);
    }

    @SneakyThrows
    public void renameFile(String username, String filename, FileRenameRequestDto fileRenameRequestDto) {
        String sourcePath = (fileRenameRequestDto.getFilepath().isEmpty())
                ? USER_PACKAGE_PREFIX + username
                : USER_PACKAGE_PREFIX + username + "/" + fileRenameRequestDto.getFilepath();

        String pathOldFile = sourcePath + "/" + filename;
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
        String folderPrefix = USER_PACKAGE_PREFIX + username + DELETED_FOLDER;

        Map<String, String> pathsMap = new HashMap<>();

        for (FileRecoverRequestDto file : files) {
            String sourcePath = (file.getFilePath().isEmpty())
                    ? folderPrefix + file.getFilename()
                    : folderPrefix + file.getFilePath() + "/" + file.getFilename();

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
                String targetFilePath = targetPath + stringUtil.getFileName(fileName);

                minio.copy(fileName, targetFilePath);

                if (isMoveOperation) {
                    minio.remove(fileName);
                }
            }
        }
    }

    private TimeResponseDto createTimeResponseDto(ZonedDateTime time) {
        return TimeResponseDto.builder()
                .day(time.toLocalDate().toString())
                .time(time.toLocalTime().toString())
                .build();
    }
}
