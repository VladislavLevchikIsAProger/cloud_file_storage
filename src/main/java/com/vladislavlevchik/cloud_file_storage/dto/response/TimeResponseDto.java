package com.vladislavlevchik.cloud_file_storage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeResponseDto {
    private String day;
    private String time;
}
