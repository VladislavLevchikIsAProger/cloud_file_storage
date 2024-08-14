package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileResponseDto;
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

    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(@RequestPart("files") List<MultipartFile> files,
                                        @RequestPart(value = "folderPath", required = false) String path) {
        String username = getUserNameFromPrincipal();

        service.uploadFile(username, path, files);

        return ResponseEntity.ok(MessageResponseDto.builder().message("File uploaded successfully").build());
    }

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

        return ResponseEntity.ok(service.listFilesInDeleted(username));
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

    @PostMapping("/files/move/deleted")
    public ResponseEntity<?> moveFilesToDeleted(@RequestBody List<FileMoveToDeletedRequestDto> files) {
        String username = getUserNameFromPrincipal();

        service.moveFilesToDeleted(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully moved to deleted")
                .build()
        );
    }

    @PostMapping("/files/recover")
    public ResponseEntity<?> recoversFiles(@RequestBody List<FileRecoverRequestDto> files) {
        String username = getUserNameFromPrincipal();

        service.recoverFiles(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully recovered")
                .build()
        );
    }

    @PatchMapping("/file/{fileName}")
    public ResponseEntity<?> renameFile(
            @PathVariable String fileName,
            @RequestBody FileRenameRequestDto fileRenameRequestDto) {
        String username = getUserNameFromPrincipal();

        service.renameFile(username, fileName, fileRenameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully renamed")
                .build()
        );
    }

    @GetMapping("/files/search")
    public ResponseEntity<?> searchFileInAllFiles(@RequestParam String fileName){
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.searchFile(username, fileName));
    }

    @GetMapping("/files/deleted/search")
    public ResponseEntity<?> searchFileInDeletedFiles(@RequestParam String fileName){
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.searchFileInDeleted(username, fileName));
    }

    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
