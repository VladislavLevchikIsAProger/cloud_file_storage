package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.docs.folders.*;
import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderChangeColorRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FolderService;
import com.vladislavlevchik.cloud_file_storage.util.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Folders")
@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService service;
    private final ValidationUtil validation;

    @CreateFolderApiDocs
    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody @Validated FolderRequestDto folderRequestDto) {
        String username = getUserNameFromPrincipal();

        service.createFolder(username, folderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder successfully created")
                .build());
    }

    @ListFoldersApiDocs
    @GetMapping
    public ResponseEntity<?> listFolders() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getList(username));
    }

    @ListFoldersForMoveApiDocs
    @GetMapping("/moved")
    public ResponseEntity<?> listFoldersForMove() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getListForMove(username));
    }

    @RenameFolderApiDocs
    @PatchMapping("{folderName}")
    public ResponseEntity<?> renameFolder(
            @PathVariable String folderName,
            @RequestBody @Validated FolderRenameRequestDto renameRequestDto) {

        validation.validateRenameFolder(folderName, renameRequestDto);

        String username = getUserNameFromPrincipal();

        service.updateName(username, folderName, renameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder successful renamed")
                .build());
    }

    @ChangeFolderColorApiDocs
    @PatchMapping("/color/{folderName}")
    public ResponseEntity<?> changeColor(
            @PathVariable String folderName,
            @RequestBody @Validated FolderChangeColorRequestDto colorRequestDto) {

        String username = getUserNameFromPrincipal();

        service.updateColor(username, folderName, colorRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder color successful change")
                .build());
    }

    @DeleteFolderApiDocs
    @DeleteMapping("{folderName}")
    public ResponseEntity<?> deleteFolder(@PathVariable String folderName) {
        String username = getUserNameFromPrincipal();

        service.deleteFolder(username, folderName);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder successful deleted")
                .build());
    }


    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
