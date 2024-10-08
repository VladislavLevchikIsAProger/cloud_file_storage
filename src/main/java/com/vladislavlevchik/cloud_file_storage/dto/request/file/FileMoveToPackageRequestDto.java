package com.vladislavlevchik.cloud_file_storage.dto.request.file;

import com.vladislavlevchik.cloud_file_storage.validation.ValidFilePath;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMoveToPackageRequestDto {

    @NotBlank(message = "Filename cannot be empty.")
    private String filename;
    @ValidFilePath(message = "The path must match the format img, img/png, files/photo/img")
    private String filePath;
    @ValidFilePath(message = "The path must match the format img, img/png, files/photo/img")
    private String newFilePath;

}
