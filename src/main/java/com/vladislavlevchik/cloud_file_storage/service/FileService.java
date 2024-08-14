package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.subfolder.SubFolderSizeResponseDto;
import com.vladislavlevchik.cloud_file_storage.exception.UploadFileException;
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

    private final FolderService folderService;

    private final StringUtil stringUtil;

    @Value("${minio.user.memory}")
    private final int userMemory;

    @SneakyThrows
    public MemoryResponseDto getMemoryInfo(String username) {
        String folderPrefix = stringUtil.getUserPrefix(username);

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
        long userMemoryInBytes = (long) userMemory * 1024 * 1024;
        long totalSizeInBytes = 0;
        long totalSizeUserInBytes = 0;

        for (MultipartFile file : files) {
            totalSizeInBytes += file.getSize();
        }

        String folderPrefix = stringUtil.getUserPrefix(username);

        for (Result<Item> result : minio.listObjects(folderPrefix)) {
            Item item = result.get();
            totalSizeUserInBytes += item.size();
        }

        if (totalSizeInBytes + totalSizeUserInBytes > userMemoryInBytes) {
            throw new UploadFileException("Total file size exceeds the available memory.");
        }

        if (path == null || path.isEmpty()) {
            path = "";
        }

        for (MultipartFile file : files) {
            String fileKey = (path.isEmpty())
                    ? folderPrefix + file.getOriginalFilename()
                    : folderPrefix + path + "/" + file.getOriginalFilename();

            minio.put(fileKey, file.getInputStream(), file.getSize(), file.getContentType());
        }
    }

    @SneakyThrows
    public List<FileResponseDto> searchFile(String username, String fileName) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = stringUtil.getUserPrefix(username);;

        String lowerCaseFileName = fileName.toLowerCase();

        for (Result<Item> itemResult : minio.listObjects(folderPrefix)) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }

            String lowerCaseObjectName = stringUtil.getFileName(objectName).toLowerCase();

            if (lowerCaseObjectName.contains(lowerCaseFileName) && !objectName.startsWith(stringUtil.getUserDeletedPrefix(username))) {
                fileList.add(buildFileResponseDto(item, username, true));
            }
        }

        return fileList;
    }

    @SneakyThrows
    public List<FileResponseDto> searchFileInDeleted(String username, String fileName) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = stringUtil.getUserDeletedPrefix(username);

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
                fileList.add(buildFileResponseDto(item, username, false));
            }
        }

        return fileList;
    }

    @SneakyThrows
    public List<FileResponseDto> listFilesInAllFiles(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = stringUtil.getUserPrefix(username);

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            String objectName = item.objectName();

            if (objectName.endsWith(".empty")) {
                continue;
            }
            if (!objectName.startsWith(stringUtil.getUserDeletedPrefix(username))) {
                fileList.add(buildFileResponseDto(item, username, true));
            }
        }

        return fileList;
    }


    @SneakyThrows
    public List<FileResponseDto> listFilesInDeleted(String username) {
        List<FileResponseDto> fileList = new ArrayList<>();

        String folderPrefix = stringUtil.getUserDeletedPrefix(username);

        Iterable<Result<Item>> objects = minio.listObjects(folderPrefix);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();

            fileList.add(buildFileResponseDto(item, username, false));
        }

        return fileList;
    }

    @SneakyThrows
    public FileAndFolderResponseDto listFilesAndDirectories(String username, String folderPath) {
        Map<String, Long> folderSizes = new HashMap<>();
        Map<String, ZonedDateTime> folderLastModified = new HashMap<>();

        List<FileResponseDto> files = new ArrayList<>();

        String folderPrefix = stringUtil.getUserPrefix(username) + folderPath + "/";

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

            if (relativePath.contains("/")) {
                int index = relativePath.indexOf('/');
                String subPath = relativePath.substring(0, index);

                folderSizes.put(subPath, folderSizes.getOrDefault(subPath, 0L) + item.size());

                ZonedDateTime currentLastModified = folderLastModified.getOrDefault(subPath, ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault()));

                folderLastModified.put(subPath, currentLastModified.isAfter(item.lastModified()) ? currentLastModified : item.lastModified());
            } else {
                files.add(buildFileResponseDto(item, username, false));
            }
        }

        return FileAndFolderResponseDto.builder()
                .folders(buildListSubFolderResponseDto(folderSizes, folderLastModified))
                .files(files)
                .build();
    }

    @SneakyThrows
    public void deleteFiles(String username, List<FileDeleteRequestDto> files) {
        String folderPrefix = stringUtil.getUserDeletedPrefix(username);

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
        String sourcePath = (files.getSource().isEmpty())
                ? stringUtil.getUserPrefixWithoutSlash(username)
                : stringUtil.getUserPrefix(username) + files.getSource();

        String targetPath = stringUtil.getUserPrefix(username) + files.getTarget() + "/";

        List<String> pathsFiles = files.getFiles().stream()
                .map(file -> sourcePath + "/" + file.getFilename())
                .toList();

        Iterable<Result<Item>> objects = minio.listObjects(sourcePath);

        for (Result<Item> itemResult : objects) {
            Item item = itemResult.get();
            String fileName = item.objectName();

            if (pathsFiles.contains(fileName)) {
                String targetFilePath = targetPath + stringUtil.getFileName(fileName);

                minio.copy(fileName, targetFilePath);

                minio.remove(fileName);
            }
        }
    }

    @SneakyThrows
    public void moveFilesToDeleted(String username, List<FileMoveToDeletedRequestDto> files) {
        for (FileMoveToDeletedRequestDto file : files) {
            String sourcePath = (file.getFilePath().isEmpty())
                    ? stringUtil.getUserPrefix(username)
                    : stringUtil.getUserPrefix(username) + file.getFilePath() + "/";

            String pathOldFile = sourcePath + file.getFilename();

            String pathNewFile = stringUtil.getUserPrefix(username) + file.getNewFilePath() + "/"
                    + (file.getFilePath().isEmpty() ? file.getFilename() : file.getFilePath() + "/" + file.getFilename());


            minio.copy(pathOldFile, pathNewFile);

            minio.remove(pathOldFile);
        }
    }

    @SneakyThrows
    public void renameFile(String username, String filename, FileRenameRequestDto fileRenameRequestDto) {
        String sourcePath = (fileRenameRequestDto.getFilepath().isEmpty())
                ? stringUtil.getUserPrefixWithoutSlash(username)
                : stringUtil.getUserPrefix(username) + fileRenameRequestDto.getFilepath();

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
        String folderPrefix = stringUtil.getUserDeletedPrefix(username);

        Map<String, String> pathsMap = buildFilePathsMapForRecovery(username, folderPrefix, files);

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

    private List<SubFolderSizeResponseDto> buildListSubFolderResponseDto(Map<String, Long> folderSizes, Map<String, ZonedDateTime> folderLastModified){
        List<SubFolderSizeResponseDto> folders = new ArrayList<>();

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

        return folders;
    }

    private Map<String, String> buildFilePathsMapForRecovery(String username, String folderPrefix, List<FileRecoverRequestDto> files) {
        Map<String, String> pathsMap = new HashMap<>();

        for (FileRecoverRequestDto file : files) {
            String sourcePath = (file.getFilePath().isEmpty())
                    ? folderPrefix + file.getFilename()
                    : folderPrefix + file.getFilePath() + "/" + file.getFilename();

            String destinationPath = (file.getFilePath().isEmpty())
                    ? stringUtil.getUserPrefix(username) + file.getFilename()
                    : stringUtil.getUserPrefix(username) + file.getFilePath() + "/" + file.getFilename();

            pathsMap.put(sourcePath, destinationPath);
        }
        return pathsMap;
    }

    private FileResponseDto buildFileResponseDto(Item item, String username, boolean inAllFiles) {
        String objectName = item.objectName();

        FileResponseDto.FileResponseDtoBuilder builder = FileResponseDto.builder()
                .filename(objectName.substring(objectName.lastIndexOf('/') + 1))
                .filePath(stringUtil.getSubdirectories(objectName))
                .size(stringUtil.convertBytesToMbOrKb(item.size()))
                .lastModified(createTimeResponseDto(item.lastModified()));

        if (inAllFiles) {
            String mainSubdirectory = stringUtil.getMainSubdirectory(objectName);
            builder.customFolderName(mainSubdirectory)
                    .color(folderService.getFolderColor(mainSubdirectory, username));
        }

        return builder.build();
    }

    private TimeResponseDto createTimeResponseDto(ZonedDateTime time) {
        return TimeResponseDto.builder()
                .day(time.toLocalDate().toString())
                .time(time.toLocalTime().toString())
                .build();
    }
}
