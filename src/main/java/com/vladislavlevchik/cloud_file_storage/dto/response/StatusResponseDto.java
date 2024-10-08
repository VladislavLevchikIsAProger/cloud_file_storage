package com.vladislavlevchik.cloud_file_storage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusResponseDto {

    private Boolean authenticated;
    private String username;

}
