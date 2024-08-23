package com.vladislavlevchik.cloud_file_storage.docs.auth;

import com.vladislavlevchik.cloud_file_storage.dto.response.StatusResponseDto;
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
        summary = "Get Authentication Status",
        description = "Retrieves the current authentication status and user details.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved authentication status",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = StatusResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "authenticated": true,
                                                "username": "user123"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized (user not authenticated)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = StatusResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "authenticated": false
                                            }
                                            """)
                        )
                )
        }
)
public @interface GetStatusApiDocs {
}
