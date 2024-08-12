package com.vladislavlevchik.cloud_file_storage.dto.request.subfolder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubFolderDeleteRequestDto {

    private String folderPath;

}
