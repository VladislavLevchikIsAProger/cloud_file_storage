package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.ListFilesRecoverRequestDto;
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
        summary = "Recover deleted files",
        description = "Allows you to restore previously deleted files to the folders from which they were deleted by providing a list of file names and paths.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "List of files to be recovered.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ListFilesRecoverRequestDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Valid request",
                                        value = """
                                    {
                                        "files": [
                                            {
                                                "filename": "EPAM-Code-Instructions.doc",
                                                "filePath": ""
                                            },
                                            {
                                                "filename": "SteamSetup1.exe",
                                                "filePath": "files/img"
                                            },
                                            {
                                                "filename": "VSCodeUserSetup-x64-1.88.0.exe",
                                                "filePath": "img"
                                            }
                                        ]
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Incorrect filePath",
                                        value = """
                                    {
                                        "files": [
                                            {
                                                "filename": "EPAM-Code-Instructions.doc",
                                                "filePath": "/"
                                            },
                                            {
                                                "filename": "SteamSetup1.exe",
                                                "filePath": "files/img"
                                            },
                                            {
                                                "filename": "VSCodeUserSetup-x64-1.88.0.exe",
                                                "filePath": "img"
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
                                                "filename": "EPAM-Code-Instructions.doc"
                                            },
                                            {
                                                "filename": "SteamSetup1.exe",
                                                "filePath": "files/img"
                                            },
                                            {
                                                "filename": "VSCodeUserSetup-x64-1.88.0.exe",
                                                "filePath": "img"
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
                                                "filePath": ""
                                            },
                                            {
                                                "filename": "SteamSetup1.exe",
                                                "filePath": "files/img"
                                            },
                                            {
                                                "filename": "VSCodeUserSetup-x64-1.88.0.exe",
                                                "filePath": "img"
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
                                        name = "Invalid request - Missing files field",
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
                        description = "Files successfully recovered",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"message\": \"Files successfully recovered\" }"
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
                                                name = "Invalid request - Incorrect filePath",
                                                value = """
                                            {
                                                "files[0].filePath": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing filePath",
                                                value = """
                                            {
                                                "files[0].filePath": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing filename",
                                                value = """
                                            {
                                                "files[0].filename": "Filename cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Empty files array",
                                                value = """
                                            {
                                                "files": "The list of files to recover cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing files field",
                                                value = """
                                            {
                                                "files": "The list of files to recover cannot be empty."
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
public @interface RecoverFilesApiDocs {
}
