package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.FolderChangeColorRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FolderForMoveResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import com.vladislavlevchik.cloud_file_storage.entity.User;
import com.vladislavlevchik.cloud_file_storage.exception.FolderNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.CustomFolderRepository;
import com.vladislavlevchik.cloud_file_storage.repository.UserRepository;
import com.vladislavlevchik.cloud_file_storage.util.BytesConverter;
import com.vladislavlevchik.cloud_file_storage.util.MinioOperationUtil;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final CustomFolderRepository customFolderRepository;
    private final UserService userService;

    private final MinioOperationUtil minio;
    private final ModelMapper mapper;
    private final BytesConverter bytesConverter;

    private final static String USER_PACKAGE_PREFIX = "user-";


    public void createFolder(String username, FolderRequestDto dto) {
        CustomFolder folder = mapper.map(dto, CustomFolder.class);

        User user = userService.getUser(username);

        folder.setUser(user);

        customFolderRepository.save(folder);
    }

    @SneakyThrows
    public void updateName(String username, String folderName, FolderRenameRequestDto renameRequestDto) {

        CustomFolder folder = customFolderRepository.findByNameAndUsername(folderName, username)
                .orElseThrow(() -> new FolderNotFoundException("Folder " + folderName + " not found"));

        folder.setName(renameRequestDto.getNewName());
        customFolderRepository.save(folder);

        String newFolderName = renameRequestDto.getNewName();

        String oldFolderPrefix = USER_PACKAGE_PREFIX + username + "/" + folderName;
        String newFolderPrefix = USER_PACKAGE_PREFIX + username + "/" + newFolderName;

        moveFolderContents(oldFolderPrefix, newFolderPrefix);

        String deletedOldFolderPrefix = USER_PACKAGE_PREFIX + username + "/deleted/" + folderName;
        String deletedNewFolderPrefix = USER_PACKAGE_PREFIX + username + "/deleted/" + newFolderName;

        moveFolderContents(deletedOldFolderPrefix, deletedNewFolderPrefix);
    }

    public void updateColor(String username, String folderName, FolderChangeColorRequestDto colorRequestDto) {
        CustomFolder folder = customFolderRepository.findByNameAndUsername(folderName, username)
                .orElseThrow(() -> new FolderNotFoundException("Folder " + folderName + " not found"));

        folder.setColor(colorRequestDto.getNewColor());
        customFolderRepository.save(folder);
    }

    @SneakyThrows
    public List<FolderResponseDto> getList(String username) {
        String folderPrefix = USER_PACKAGE_PREFIX + username;

        List<CustomFolder> folders = userService.getListFolders(username);

        return folders.stream()
                .map(folder -> getFolderResponse(folderPrefix, folder))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public List<FolderForMoveResponseDto> getListForMove(String username) {
        List<CustomFolder> folders = userService.getListFolders(username);

        return folders.stream()
                .map(folder -> mapper.map(folder, FolderForMoveResponseDto.class))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void createSubFolder(String username, SubFolderRequestDto subFolderRequestDto) {
        String folderPath = USER_PACKAGE_PREFIX + username + "/"
                + subFolderRequestDto.getFolderPath() + "/" + subFolderRequestDto.getName() + "/";

        minio.createEmptyPackage(folderPath);
    }

    public String getFolderColor(String folderName, String username) {
        return customFolderRepository.findColorByNameAndUsername(folderName, username);
    }

    @SneakyThrows
    private void moveFolderContents(String oldFolderPrefix, String newFolderPrefix) {
        Iterable<Result<Item>> items = minio.listObjects(oldFolderPrefix);

        for (Result<Item> result : items) {
            Item item = result.get();
            String oldObjectName = item.objectName();
            String newObjectName = oldObjectName.replace(oldFolderPrefix, newFolderPrefix);

            minio.copy(oldObjectName, newObjectName);
            minio.remove(oldObjectName);
        }
    }

    @SneakyThrows
    private FolderResponseDto getFolderResponse(String folderPrefix, CustomFolder folder) {
        Iterable<Result<Item>> results = minio.listObjects(folderPrefix + "/" + folder.getName());

        long totalSize = 0;
        long itemCount = 0;

        for (Result<Item> result : results) {
            Item item = result.get();
            if (!item.objectName().endsWith(".empty")) {
                itemCount++;
                totalSize += item.size();
            }
        }

        return FolderResponseDto.builder()
                .name(folder.getName())
                .color(folder.getColor())
                .size(bytesConverter.convertToMbOrKb(totalSize))
                .filesNumber(String.valueOf(itemCount))
                .build();
    }
}
