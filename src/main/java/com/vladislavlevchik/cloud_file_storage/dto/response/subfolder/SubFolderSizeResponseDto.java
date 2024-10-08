package com.vladislavlevchik.cloud_file_storage.dto.response.subfolder;

import com.vladislavlevchik.cloud_file_storage.dto.response.TimeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubFolderSizeResponseDto {

    private String name;
    private String size;
    private TimeResponseDto lastModified;

}
