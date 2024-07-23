package com.vladislavlevchik.cloud_file_storage.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {

    private String username;
    private String password;

}
