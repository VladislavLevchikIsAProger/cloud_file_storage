package com.vladislavlevchik.cloud_file_storage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMoveToDeletedRequestDto {

    private String filename;
    private String filePath;
    private String newFilePath;

}
