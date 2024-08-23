package com.vladislavlevchik.cloud_file_storage.dto.request.folder;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderRenameRequestDto {

    @NotBlank(message = "Filename cannot be empty.")
    private String newName;

}
