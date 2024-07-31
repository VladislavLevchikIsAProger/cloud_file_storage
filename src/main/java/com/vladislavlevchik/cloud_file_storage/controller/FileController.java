package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/memory/{username}")
    public ResponseEntity<?> userMemoryInfo(@PathVariable String username) {
        return ResponseEntity.ok(service.getMemoryInfo(username));
    }
}
