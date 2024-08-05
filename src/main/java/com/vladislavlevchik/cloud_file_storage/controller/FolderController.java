package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.FolderRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService service;

    @PostMapping("/create")
    public ResponseEntity<?> createPackage(@RequestBody FolderRequestDto folderRequestDto) {

        service.createFolder(folderRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Folder successfully created")
                .build());
    }

    @GetMapping
    public ResponseEntity<?> listPackages(@RequestParam String username){

        return ResponseEntity.ok(service.getList(username));
    }

}
