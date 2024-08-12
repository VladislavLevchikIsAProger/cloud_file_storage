package com.vladislavlevchik.cloud_file_storage.dto.response.file;

import com.vladislavlevchik.cloud_file_storage.dto.response.TimeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDto {

    private String filename;
    private String filePath;
    private String size;
    private TimeResponseDto lastModified;
    private String color;
    private String customFolderName;

}
