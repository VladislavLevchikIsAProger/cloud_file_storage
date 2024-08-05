package com.vladislavlevchik.cloud_file_storage.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileCopyRequestDto {

    private String source;
    private String target;
    private List<FileNameRequestDto> files;

}
