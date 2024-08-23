package com.vladislavlevchik.cloud_file_storage.dto.request.file;

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
public class ListFilesDeleteRequestDto {

    @NotEmpty(message = "The list of files to delete cannot be empty.")
    @Valid
    private List<FileDeleteRequestDto> files;
}
