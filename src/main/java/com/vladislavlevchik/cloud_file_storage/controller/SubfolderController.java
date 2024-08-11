package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderRenameRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subfolders")
@RequiredArgsConstructor
public class SubfolderController {

    private final FolderService service;

    @PostMapping()
    public ResponseEntity<?> createSubFolder(@RequestBody SubFolderRequestDto subFolderRequestDto) {
        String username = getUserNameFromPrincipal();

        service.createSubFolder(username, subFolderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successfully created")
                .build());
    }

    @PatchMapping()
    public ResponseEntity<?> renameSubfolder(
            @RequestBody SubFolderRenameRequestDto renameRequestDto) {

        String username = getUserNameFromPrincipal();

        service.updateSubfolderName(username, renameRequestDto.getOldName(), renameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successful renamed")
                .build());
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteSubfolder(
            @RequestBody SubFolderDeleteRequestDto deleteRequestDto) {

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
