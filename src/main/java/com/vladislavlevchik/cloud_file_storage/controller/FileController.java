package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.MessageDto;
import com.vladislavlevchik.cloud_file_storage.dto.MoveFileRequestDto;
import com.vladislavlevchik.cloud_file_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService service;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file,
                                        @RequestPart("user") String username,
                                        @RequestPart("folderPath") String path) {
        service.uploadFile(username, path, file);

        return ResponseEntity.ok(MessageDto.builder().message("File uploaded successfully").build());
    }

    @GetMapping("/memory/{username}")
    public ResponseEntity<?> userMemoryInfo(@PathVariable String username) {
        return ResponseEntity.ok(service.getMemoryInfo(username));
    }

    @PostMapping("/move")
    public ResponseEntity<?> moveFile(@RequestBody MoveFileRequestDto request) {
        service.moveToPackage(
                "user-" + request.getUsername() + request.getSourceFolder() + "/" + request.getFileName(),
                "user-" + request.getUsername() + request.getTargetFolder() + "/" + request.getFileName()
        );
        return ResponseEntity.ok(MessageDto.builder().message("The file has been successfully moved").build());
    }

    @PostMapping("/delete")
    public ResponseEntity<?> moveToDeleted(@RequestBody MoveFileRequestDto request) {
        service.moveToPackage(
                "user-" + request.getUsername() + request.getSourceFolder() + "/" + request.getFileName(),
                "user-" + request.getUsername() + "/deleted" + request.getSourceFolder() + "/" + request.getFileName()
        );

        return ResponseEntity.ok(MessageDto.builder().message("The file has been successfully moved to deleted").build());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getAllFilesName(@PathVariable String username) {
        List<String> allFiles = service.getAllFiles(username);

        return ResponseEntity.ok(allFiles);
    }
}
