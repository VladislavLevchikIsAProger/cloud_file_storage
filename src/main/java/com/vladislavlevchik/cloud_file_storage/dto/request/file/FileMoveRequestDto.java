package com.vladislavlevchik.cloud_file_storage.dto.request.file;

import com.vladislavlevchik.cloud_file_storage.validation.ValidFilePath;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMoveRequestDto {

    @ValidFilePath(message = "The path must match the format img, img/png, files/photo/img")
    private String source;
    @ValidFilePath(message = "The path must match the format img, img/png, files/photo/img")
    private String target;

    @NotEmpty(message = "The list of files to delete cannot be empty.")
    @Valid
    private List<FileNameRequestDto> files;
}
