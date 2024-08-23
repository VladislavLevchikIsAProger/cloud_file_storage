package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.docs.subfolder.CreateSubfolderApiDocs;
import com.vladislavlevchik.cloud_file_storage.docs.subfolder.DeleteSubfolderApiDocs;
import com.vladislavlevchik.cloud_file_storage.docs.subfolder.RenameSubfolderApiDocs;
import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subfolders")
@RequiredArgsConstructor
public class SubfolderController {

    private final FolderService service;

    @CreateSubfolderApiDocs
    @PostMapping()
    public ResponseEntity<?> createSubFolder(@RequestBody @Validated SubFolderRequestDto subFolderRequestDto) {
        String username = getUserNameFromPrincipal();

        service.createSubFolder(username, subFolderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successfully created")
                .build());
    }

    @RenameSubfolderApiDocs
    @PatchMapping()
    public ResponseEntity<?> renameSubfolder(
            @RequestBody @Validated SubFolderRenameRequestDto renameRequestDto) {
        String username = getUserNameFromPrincipal();

        service.updateSubfolderName(username, renameRequestDto.getOldName(), renameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successful renamed")
                .build());
    }

    @DeleteSubfolderApiDocs
    @DeleteMapping()
    public ResponseEntity<?> deleteSubfolder(
            @RequestBody @Validated SubFolderDeleteRequestDto deleteRequestDto) {

        String username = getUserNameFromPrincipal();

        service.deleteSubFolder(username, deleteRequestDto.getFolderPath());

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successful deleted")
                .build());
    }

    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
