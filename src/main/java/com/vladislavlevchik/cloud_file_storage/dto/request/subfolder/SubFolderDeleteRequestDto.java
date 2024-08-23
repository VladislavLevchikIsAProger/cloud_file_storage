package com.vladislavlevchik.cloud_file_storage.dto.request.subfolder;

import com.vladislavlevchik.cloud_file_storage.validation.ValidFilePath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubFolderDeleteRequestDto {

    @ValidFilePath(message = "The path must match the format img/png, files/photo/img")
    private String folderPath;

}
