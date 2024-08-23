package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.FileMoveRequestDto;
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
        summary = "Move files in source to target",
        description = "Moves specified files from the source path to the target path.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Details of files to be moved, including source and target paths.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FileMoveRequestDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Valid request",
                                        value = """
                                    {
                                        "source": "img",
                                        "target": "img/backup",
                                        "files": [
                                            {
                                                "filename": "photo.jpg"
                                            },
                                            {
                                                "filename": "document.pdf"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing source path",
                                        value = """
                                    {
                                        "target": "img/backup",
                                        "files": [
                                            {
                                                "filename": "photo.jpg"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing target path",
                                        value = """
                                    {
                                        "source": "img",
                                        "files": [
                                            {
                                                "filename": "photo.jpg"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing files",
                                        value = """
                                    {
                                        "source": "img",
                                        "target": "img/backup"
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Empty body",
                                        value = """
                                    {}
                                    """
                                )
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Files successfully migrated",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"message\": \"Files successfully migrated\" }"
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid input data)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Invalid request - Files list empty",
                                                value = """
                                            {
                                                "files": "The list of files to delete cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Files list not provided",
                                                value = """
                                            {
                                                "files": "The list of files to delete cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Filename missing",
                                                value = """
                                            {
                                                "files[0].filename": "Filename cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Target path missing",
                                                value = """
                                            {
                                                "target": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Source path missing",
                                                value = """
                                            {
                                                "source": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Incorrect source path",
                                                value = """
                                            {
                                                "source": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        )
                                }
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
public @interface MoveFilesInSourceToTargetApiDocs {
}
