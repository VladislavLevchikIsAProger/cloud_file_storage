package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.ListFilesDeleteRequestDto;
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
        summary = "Delete files",
        description = "Deletes specified files from the user's storage.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "List of files to be deleted.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ListFilesDeleteRequestDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Valid request",
                                        value = """
                                    {
                                        "files": [
                                            {
                                                "filename": "example.txt",
                                                "filePath": "docs"
                                            },
                                            {
                                                "filename": "image.img",
                                                "filePath": "img/photo"
                                            },
                                            {
                                                "filename": "hello.txt",
                                                "filePath": ""
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing filename",
                                        value = """
                                    {
                                        "files": [
                                            {
                                                "filePath": "img/img"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing filePath",
                                        value = """
                                    {
                                        "files": [
                                            {
                                                "filename": "Wallpaper.jpg"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Empty files array",
                                        value = """
                                    {
                                        "files": []
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
                        description = "Files successfully deleted",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"message\": \"Files successfully deleted\" }"
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
                                                name = "Invalid request - Missing filePath",
                                                value = """
                                            {
                                                "files[0].filePath": "The path must match the format img, img/png, files/photo/img"
                                            }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing filename",
                                                value = """
                                            {
                                                "files[0].filename": "Filename cannot be empty."
                                            }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Empty files array",
                                                value = """
                                            {
                                                "files": "The list of files to delete cannot be empty."
                                            }
                                            """
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - No files field",
                                                value = """
                                            {
                                                "files": "The list of files to delete cannot be empty."
                                            }
                                            """
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
public @interface DeleteFilesApiDocs {
}
