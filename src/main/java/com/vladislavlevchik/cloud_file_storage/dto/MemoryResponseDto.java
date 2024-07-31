package com.vladislavlevchik.cloud_file_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoryResponseDto {

    private BigDecimal totalSize;
    private Integer userMemory;

}
