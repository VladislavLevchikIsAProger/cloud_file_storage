package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.MoveFileRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileController {

    private final FileService service;

    //TODO не сделано для множества файлов
    //TODO валидация не сделана
    @PostMapping("/file/move")
    public ResponseEntity<?> moveFile(@RequestBody MoveFileRequestDto request) {
        service.moveToPackage(
                "user-" + request.getUsername() + request.getSourceFolder() + "/" + request.getFileName(),
                "user-" + request.getUsername() + request.getTargetFolder() + "/" + request.getFileName()
        );
        return ResponseEntity.ok(MessageResponseDto.builder().message("The file has been successfully moved").build());
    }

    //TODO не сделано для множества файлов
    //TODO валидация не сделана
    @PostMapping("/file/delete")
    public ResponseEntity<?> moveToDeleted(@RequestBody MoveFileRequestDto request) {
        service.moveToPackage(
                "user-" + request.getUsername() + request.getSourceFolder() + "/" + request.getFileName(),
                "user-" + request.getUsername() + "/deleted" + request.getSourceFolder() + "/" + request.getFileName()
        );

        return ResponseEntity.ok(MessageResponseDto.builder().message("The file has been successfully moved to deleted").build());
    }

    //TODO сделано для множества файлов
    //TODO Валидация не сделана
    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("files") List<MultipartFile> files,
                                        @RequestPart("user") String username,
                                        @RequestPart(value = "folderPath", required = false) String path) {

        service.uploadFile(username, path, files);

        return ResponseEntity.ok(MessageResponseDto.builder().message("File uploaded successfully").build());
    }

    //TODO валидация не сделана
    @GetMapping("/files/memory/{username}")
    public ResponseEntity<?> userMemoryInfo(@PathVariable String username) {
        return ResponseEntity.ok(service.getMemoryInfo(username));
    }

    @GetMapping("/files/user/{username}")
    public ResponseEntity<?> getAllFilesName(@PathVariable String username) {
        List<String> allFiles = service.getAllFiles(username);

        return ResponseEntity.ok(allFiles);
    }

    @GetMapping("/files/all")
    public ResponseEntity<?> listFilesInAllFilesFolder(
            @RequestParam String username) {

        List<FileResponseDto> files = service.listFilesInAllFiles(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/deleted")
    public ResponseEntity<?> listFilesInDeleteFolder(
            @RequestParam String username) {

        List<FileResponseDto> files = service.listFilesInDeleted(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files")
    public ResponseEntity<?> listFilesAndDirectories(
            @RequestParam String path,
            @RequestParam String username) {

        FileAndFolderResponseDto filesAndFolders = service.listFilesAndDirectories(username, path);
        return ResponseEntity.ok(filesAndFolders);
    }
}
