package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Search files by name",
        description = "Searches for all files that contain the provided substring in their filenames. If no substring is provided or if it's an empty string, an error will be returned.",
        parameters = {
                @Parameter(
                        name = "fileName",
                        description = "The substring to search for within filenames. If omitted or empty, an error will be returned.",
                        required = true,
                        example = "st"
                )
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Files found successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = List.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Files containing 'f'",
                                                value = """
                        [
                            {
                                "filename": "FM24 Increase Realism Megapack JULY UPDATE(05.07.24).zip",
                                "filePath": "",
                                "size": "16,12MB",
                                "lastModified": {
                                    "day": "2024-08-14",
                                    "time": "09:37:22.726"
                                },
                                "color": null,
                                "customFolderName": ""
                            },
                            {
                                "filename": "dmdev_java_roadmap.pdf",
                                "filePath": "",
                                "size": "2,70MB",
                                "lastModified": {
                                    "day": "2024-08-14",
                                    "time": "20:39:12.849"
                                },
                                "color": null,
                                "customFolderName": ""
                            }
                        ]"""
                                        ),
                                        @ExampleObject(
                                                name = "Files containing 'st'",
                                                value = """
                        [
                            {
                                "filename": "EPAM-Code-Instructions.doc",
                                "filePath": "",
                                "size": "49,00KB",
                                "lastModified": {
                                    "day": "2024-08-17",
                                    "time": "16:58:28.209"
                                },
                                "color": null,
                                "customFolderName": ""
                            },
                            {
                                "filename": "SteamSetup1.exe",
                                "filePath": "files/img",
                                "size": "2,27MB",
                                "lastModified": {
                                    "day": "2024-08-17",
                                    "time": "16:58:28.271"
                                },
                                "color": null,
                                "customFolderName": "files"
                            }
                        ]"""
                                        )
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., missing or empty fileName)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Missing fileName parameter",
                                                value = """
                        {
                            "message": "Filename must not be empty"
                        }"""
                                        ),
                                        @ExampleObject(
                                                name = "Empty fileName parameter",
                                                value = """
                        {
                            "message": "Filename must not be empty"
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
public @interface SearchFilesInAllFilesApiDocs {
}
