package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.response.MemoryResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Get user memory info",
        description = "Retrieves memory usage information for the currently authenticated user.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Memory information retrieved successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MemoryResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"totalSize\": 124.01, \"userMemory\": 500 }"
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"authenticated\": \"false\" }"
                                )
                        )
                )
        }
)
public @interface UserMemoryApiDocs {
}
