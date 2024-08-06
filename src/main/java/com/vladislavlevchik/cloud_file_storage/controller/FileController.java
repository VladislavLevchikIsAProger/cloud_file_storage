package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.request.FileCopyRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FileDeleteRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FileMoveRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.request.FileRenameRequestDto;
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

    @DeleteMapping("/files")
    public ResponseEntity<?> deleteFiles(
            @RequestParam String username,
            @RequestBody List<FileDeleteRequestDto> files) {

        service.deleteFiles(username, files);

        return ResponseEntity.ok(
                MessageResponseDto.builder()
                        .message("Files successfully deleted")
                        .build()
        );
    }

    @PostMapping("/files/move")
    public ResponseEntity<?> moveFiles(
            @RequestParam String username,
            @RequestBody FileMoveRequestDto files) {

        service.moveFiles(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully migrated")
                .build()
        );
    }

    @PostMapping("/files/copy")
    public ResponseEntity<?> copyFiles(
            @RequestParam String username,
            @RequestBody FileCopyRequestDto fileCopyRequestDto) {

        service.copyFiles(username, fileCopyRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully copied")
                .build()
        );
    }

    @PostMapping("/file/rename")
    public ResponseEntity<?> renameFile(
            @RequestParam String username,
            @RequestBody FileRenameRequestDto fileRenameRequestDto) {

        service.renameFile(username, fileRenameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully renamed")
                .build()
        );
    }
}
