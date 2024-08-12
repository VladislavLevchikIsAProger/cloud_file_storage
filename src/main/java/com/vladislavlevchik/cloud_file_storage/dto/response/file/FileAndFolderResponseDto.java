package com.vladislavlevchik.cloud_file_storage.dto.response.file;

import com.vladislavlevchik.cloud_file_storage.dto.response.subfolder.SubFolderSizeResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAndFolderResponseDto {

    private List<SubFolderSizeResponseDto> folders;
    private List<FileResponseDto> files;

}
