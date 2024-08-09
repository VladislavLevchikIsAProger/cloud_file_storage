package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileController {

    private final FileService service;

    //TODO сделано для множества файлов
    //TODO Валидация не сделана
    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("files") List<MultipartFile> files,
                                        @RequestPart(value = "folderPath", required = false) String path) {
        String username = getUserNameFromPrincipal();

        service.uploadFile(username, path, files);

        return ResponseEntity.ok(MessageResponseDto.builder().message("File uploaded successfully").build());
    }

    //TODO валидация не сделана
    @GetMapping("/files/memory")
    public ResponseEntity<?> userMemoryInfo() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getMemoryInfo(username));
    }

    @GetMapping("/files/all")
    public ResponseEntity<?> listFilesInAllFilesFolder() {
        String username = getUserNameFromPrincipal();

        List<FileResponseDto> files = service.listFilesInAllFiles(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files/deleted")
    public ResponseEntity<?> listFilesInDeleteFolder() {
        String username = getUserNameFromPrincipal();

        List<FileResponseDto> files = service.listFilesInDeleted(username);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/files")
    public ResponseEntity<?> listFilesAndDirectories(@RequestParam String path) {
        String username = getUserNameFromPrincipal();

        FileAndFolderResponseDto filesAndFolders = service.listFilesAndDirectories(username, path);
        return ResponseEntity.ok(filesAndFolders);
    }

    @DeleteMapping("/files")
    public ResponseEntity<?> deleteFiles(@RequestBody List<FileDeleteRequestDto> files) {
        String username = getUserNameFromPrincipal();

        service.deleteFiles(username, files);

        return ResponseEntity.ok(
                MessageResponseDto.builder()
                        .message("Files successfully deleted")
                        .build()
        );
    }

    @PostMapping("/files/move")
    public ResponseEntity<?> moveFiles(@RequestBody FileMoveRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.moveFiles(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully migrated")
                .build()
        );
    }

    @PostMapping("/files/copy")
    public ResponseEntity<?> copyFiles(@RequestBody FileCopyRequestDto fileCopyRequestDto) {
        String username = getUserNameFromPrincipal();

        service.copyFiles(username, fileCopyRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully copied")
                .build()
        );
    }

    @PostMapping("/files/recover")
    public ResponseEntity<?> recoversFiles(@RequestBody List<FileRecoverRequestDto> files) {
        String username = getUserNameFromPrincipal();

        service.recoverFiles(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully migrated")
                .build()
        );
    }

    @PostMapping("/file/rename")
    public ResponseEntity<?> renameFile(@RequestBody FileRenameRequestDto fileRenameRequestDto) {
        String username = getUserNameFromPrincipal();

        service.renameFile(username, fileRenameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully renamed")
                .build()
        );
    }

    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
