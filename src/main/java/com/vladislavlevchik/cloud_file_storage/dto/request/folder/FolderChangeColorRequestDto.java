package com.vladislavlevchik.cloud_file_storage.dto.request.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderChangeColorRequestDto {

    @NotBlank(message = "File color cannot be empty.")
    @Pattern(regexp = "^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$", message = "Color must be a valid HEX code (e.g. #ffffff or #fff).")
    private String newColor;

}
