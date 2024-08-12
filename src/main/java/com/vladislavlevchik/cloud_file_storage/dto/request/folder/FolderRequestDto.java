package com.vladislavlevchik.cloud_file_storage.dto.request.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderRequestDto {

    private String name;
    private String color;

}
