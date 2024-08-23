package com.vladislavlevchik.cloud_file_storage.service;

import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderChangeColorRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.folder.FolderForMoveResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.folder.FolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.entity.CustomFolder;
import com.vladislavlevchik.cloud_file_storage.exception.FolderAlreadyExistException;
import com.vladislavlevchik.cloud_file_storage.exception.FolderNotFoundException;
import com.vladislavlevchik.cloud_file_storage.repository.CustomFolderRepository;
import com.vladislavlevchik.cloud_file_storage.util.StringUtil;
import com.vladislavlevchik.cloud_file_storage.util.MinioOperationUtil;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final CustomFolderRepository customFolderRepository;
    private final UserService userService;

    private final MinioOperationUtil minio;
    private final ModelMapper mapper;
    private final StringUtil stringUtil;

    public void createFolder(String username, FolderRequestDto dto) {
        CustomFolder folder = mapper.map(dto, CustomFolder.class);

        folder.setUser(userService.getUser(username));

        saveFolder(folder);
    }

    public void updateName(String username, String folderName, FolderRenameRequestDto renameRequestDto) {
        CustomFolder folder = findFolder(username, folderName);

        folder.setName(renameRequestDto.getNewName());

        saveFolder(folder);

        updateFolderName(username, folderName, renameRequestDto.getNewName());
    }

    @SneakyThrows
    public void updateSubfolderName(String username, String folderName, SubFolderRenameRequestDto renameRequestDto) {
        updateFolderName(username, folderName, renameRequestDto.getNewName());
    }

    public void updateColor(String username, String folderName, FolderChangeColorRequestDto colorRequestDto) {
        CustomFolder folder = findFolder(username, folderName);

        folder.setColor(colorRequestDto.getNewColor());
        customFolderRepository.save(folder);
    }

    @SneakyThrows
    public List<FolderResponseDto> getList(String username) {
        String folderPrefix = stringUtil.getUserPrefix(username);

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
        String folderPath = stringUtil.getUserPrefix(username)
                + subFolderRequestDto.getFolderPath() + "/" + subFolderRequestDto.getName() + "/";

        minio.createEmptyPackage(folderPath);
    }

    @SneakyThrows
    public void deleteFolder(String username, String folderName) {
        CustomFolder folder = findFolder(username, folderName);

        customFolderRepository.delete(folder);

        delete(username, folderName);
    }

    @SneakyThrows
    public void deleteSubFolder(String username, String folderName) {
        delete(username, folderName);
    }

    @SneakyThrows
    private void delete(String username, String folderName) {
        String folderPrefix = stringUtil.getUserPrefix(username) + folderName + "/";

        String deleteFolder = stringUtil.getUserDeletedPrefix(username);

        Iterable<Result<Item>> items = minio.listObjects(folderPrefix);

        for (Result<Item> result : items) {
            Item item = result.get();
            String oldFileName = item.objectName();

            String newFilename = deleteFolder + stringUtil.getFileName(oldFileName);

            if (oldFileName.endsWith(".empty")) {
                minio.remove(oldFileName);
                continue;
            }

            minio.copy(oldFileName, newFilename);
            minio.remove(oldFileName);
        }

        items = minio.listObjects(deleteFolder + folderName);

        for (Result<Item> result : items) {
            Item item = result.get();

            String oldFileName = item.objectName();
            String newFilename = deleteFolder + stringUtil.getFileName(oldFileName);

            minio.copy(oldFileName, newFilename);
            minio.remove(oldFileName);
        }
    }

    public String getFolderColor(String folderName, String username) {
        return customFolderRepository.findColorByNameAndUsername(folderName, username);
    }

    private void updateFolderName(String username, String oldFolderName, String newFolderName) {
        String oldFolderPrefix = stringUtil.getUserPrefix(username) + oldFolderName + "/";
        String newFolderPrefix = stringUtil.getUserPrefix(username) + newFolderName + "/";

        moveFolderContents(oldFolderPrefix, newFolderPrefix);

        String deletedOldFolderPrefix = stringUtil.getUserDeletedPrefix(username) + oldFolderName + "/";
        String deletedNewFolderPrefix = stringUtil.getUserDeletedPrefix(username) + newFolderName + "/";

        moveFolderContents(deletedOldFolderPrefix, deletedNewFolderPrefix);
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

    private CustomFolder findFolder(String username, String folderName) {
        return customFolderRepository.findByNameAndUsername(folderName, username)
                .orElseThrow(() -> new FolderNotFoundException("Folder " + folderName + " not found"));
    }

    @SneakyThrows
    private FolderResponseDto getFolderResponse(String folderPrefix, CustomFolder folder) {
        Iterable<Result<Item>> results = minio.listObjects(folderPrefix + folder.getName() + "/");

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
                .size(stringUtil.convertBytesToMbOrKb(totalSize))
                .filesNumber(String.valueOf(itemCount))
                .build();
    }

    private void saveFolder(CustomFolder folder) {
        try {
            customFolderRepository.save(folder);
        } catch (DataIntegrityViolationException ex) {
            throw new FolderAlreadyExistException("Folder with name=" + folder.getName() + " already exist");
        }
    }
}
