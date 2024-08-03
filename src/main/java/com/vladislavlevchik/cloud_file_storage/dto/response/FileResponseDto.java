package com.vladislavlevchik.cloud_file_storage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponseDto {

    private String filename;
    private String filePath;
    private String size;
    private TimeResponseDto lastModified;


}
