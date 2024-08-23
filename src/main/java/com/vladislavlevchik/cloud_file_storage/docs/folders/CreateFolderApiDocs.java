package com.vladislavlevchik.cloud_file_storage.docs.folders;

import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRequestDto;
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
        summary = "Create a new folder",
        description = "Creates a new folder in minio and saves the metadata to the database",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Folder creation request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FolderRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = "{ \"name\": \"Me\", \"color\": \"#D23434\" }"),
                                @ExampleObject(name = "Missing Name", value = "{ \"color\": \"#D23434\" }"),
                                @ExampleObject(name = "Missing Color", value = "{ \"name\": \"Me\" }"),
                                @ExampleObject(name = "Empty Name", value = "{ \"name\": \"\", \"color\": \"#D23434\" }"),
                                @ExampleObject(name = "Invalid Color Format", value = "{ \"name\": \"Me\", \"color\": \"#D234341\" }"),
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Folder successfully created",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = "{ \"message\": \"Folder successfully created\" }")
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid request data)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = {
                                        @ExampleObject(name = "Missing Name", value = "{ \"name\": \"Filename cannot be empty.\" }"),
                                        @ExampleObject(name = "Missing Color", value = "{ \"color\": \"File color cannot be empty.\" }"),
                                        @ExampleObject(name = "Empty Name", value = "{ \"name\": \"Filename cannot be empty.\" }"),
                                        @ExampleObject(name = "Invalid Color Format", value = "{ \"color\": \"Color must be a valid HEX code (e.g. #ffffff or #fff).\" }")
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflict (e.g., folder already exists)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = "{ \"message\": \"Folder with name=Me already exist\" }")
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
public @interface CreateFolderApiDocs {
}
