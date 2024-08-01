package com.vladislavlevchik.cloud_file_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveFileRequest {

    private String sourceFolder;
    private String targetFolder;
    private String fileName;
    private String username;
}
