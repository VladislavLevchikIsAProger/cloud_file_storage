package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.docs.files.*;
import com.vladislavlevchik.cloud_file_storage.dto.request.file.*;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileAndFolderResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.FileService;
import com.vladislavlevchik.cloud_file_storage.util.ValidationUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Files")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileController {

    private final FileService service;
    private final ValidationUtil validation;

    @Hidden
    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(@RequestPart(value = "files", required = false) List<MultipartFile> files,
                                        @RequestPart(value = "folderPath", required = false) String path) {
        validation.validateFiles(files);
        validation.validateUploadPath(path);

        String username = getUserNameFromPrincipal();

        service.uploadFile(username, path, files);

        return ResponseEntity.ok(MessageResponseDto.builder().message("File uploaded successfully").build());
    }


    @UserMemoryApiDocs
    @GetMapping("/files/memory")
    public ResponseEntity<?> userMemoryInfo() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.getMemoryInfo(username));
    }

    @ListAllFilesApiDocs
    @GetMapping("/files/all")
    public ResponseEntity<?> listFilesInAllFilesFolder() {
        String username = getUserNameFromPrincipal();

        List<FileResponseDto> files = service.listFilesInAllFiles(username);
        return ResponseEntity.ok(files);
    }

    @ListDeletedFilesApiDocs
    @GetMapping("/files/deleted")
    public ResponseEntity<?> listFilesInDeleteFolder() {
        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.listFilesInDeleted(username));
    }

    @ListFilesAndDirectoriesApiDocs
    @GetMapping("/files")
    public ResponseEntity<?> listFilesAndDirectories(@RequestParam(value = "path", required = false) String path) {
        validation.validatePath(path);

        String username = getUserNameFromPrincipal();

        FileAndFolderResponseDto filesAndFolders = service.listFilesAndDirectories(username, path);
        return ResponseEntity.ok(filesAndFolders);
    }

    @DeleteFilesApiDocs
    @DeleteMapping("/files")
    public ResponseEntity<?> deleteFiles(@RequestBody @Validated ListFilesDeleteRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.deleteFiles(username, files.getFiles());

        return ResponseEntity.ok(
                MessageResponseDto.builder()
                        .message("Files successfully deleted")
                        .build()
        );
    }

    @MoveFilesInSourceToTargetApiDocs
    @PostMapping("/files/move")
    public ResponseEntity<?> moveFiles(@RequestBody @Validated FileMoveRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.moveFiles(username, files);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully migrated")
                .build()
        );
    }

    @MoveToDeletedApiDocs
    @PostMapping("/files/move/deleted")
    public ResponseEntity<?> moveFilesToDeleted(@RequestBody @Validated ListFilesMoveToPackageRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.moveFilesToDeleted(username, files.getFiles());

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully moved to deleted")
                .build()
        );
    }

    @MoveToFolderApiDocs
    @PostMapping("/files/all/move")
    public ResponseEntity<?> moveFilesToPackage(@RequestBody @Validated ListFilesMoveToPackageRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.moveFilesToPackage(username, files.getFiles());

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully moved")
                .build()
        );
    }

    @RecoverFilesApiDocs
    @PostMapping("/files/recover")
    public ResponseEntity<?> recoversFiles(@RequestBody @Validated ListFilesRecoverRequestDto files) {
        String username = getUserNameFromPrincipal();

        service.recoverFiles(username, files.getFiles());

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully recovered")
                .build()
        );
    }

    @RenameFileApiDocs
    @PatchMapping("/files/{fileName}")
    public ResponseEntity<?> renameFile(
            @PathVariable String fileName,
            @RequestBody @Validated FileRenameRequestDto fileRenameRequestDto) {
        validation.validateFilename(fileName);

        String username = getUserNameFromPrincipal();

        service.renameFile(username, fileName, fileRenameRequestDto);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message("Files successfully renamed")
                .build()
        );
    }

    @SearchFilesInAllFilesApiDocs
    @GetMapping("/files/search")
    public ResponseEntity<?> searchFileInAllFiles(@RequestParam(value = "fileName", required = false) String fileName) {
        validation.validateFilename(fileName);

        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.searchFile(username, fileName));
    }

    @SearchFilesInDeletedApiDocs
    @GetMapping("/files/deleted/search")
    public ResponseEntity<?> searchFileInDeletedFiles(@RequestParam String fileName) {
        validation.validateFilename(fileName);

        String username = getUserNameFromPrincipal();

        return ResponseEntity.ok(service.searchFileInDeleted(username, fileName));
    }

    @DownloadFileApiDocs
    @GetMapping("/files/download")
    public ResponseEntity<?> downloadFile(@RequestParam String fileName) {
        validation.validateFilename(fileName);

        String username = getUserNameFromPrincipal();

        Resource file = service.downloadFile(username, fileName);

        String finalFileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + finalFileName + "\"")
                .body(file);
    }

    private String getUserNameFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
