package com.vladislavlevchik.cloud_file_storage.dto.response.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderResponseDto {

    private String name;
    private String color;
    private String size;
    private String filesNumber;

}
