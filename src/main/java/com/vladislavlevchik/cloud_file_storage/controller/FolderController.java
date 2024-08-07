package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.SubFolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService service;

    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody FolderRequestDto folderRequestDto) {
        String username = getUserNameFromPrincipal();

        service.createFolder(username, folderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder successfully created")
                .build());
    }

    @PostMapping("/create/subfolder")
    public ResponseEntity<?> createSubFolder(@RequestBody SubFolderRequestDto subFolderRequestDto) {
        String username = getUserNameFromPrincipal();

        service.createSubFolder(username, subFolderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Subfolder successfully created")
                .build());
    }

    @GetMapping
    public ResponseEntity<?> listFolders(){
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getList(username));
    }

    @GetMapping("/moved")
    public ResponseEntity<?> listFoldersForMove(){
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getListForMove(username));
    }

    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
