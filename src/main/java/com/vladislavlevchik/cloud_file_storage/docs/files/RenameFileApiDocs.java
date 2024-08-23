package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.request.file.FileRenameRequestDto;
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
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Rename a file",
        description = "Allows the user to rename a file by providing the current filename and the new filename.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Current filename and new filename details.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FileRenameRequestDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "Valid request",
                                        value = """
                                    {
                                        "filepath": "img",
                                        "newFileName": "MediaGet.exe"
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Incorrect filepath",
                                        value = """
                                    {
                                        "filepath": "/",
                                        "newFileName": "MediaGet.exe"
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing filepath",
                                        value = """
                                    {
                                        "newFileName": "MediaGet.exe"
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Missing newFileName",
                                        value = """
                                    {
                                        "filepath": "img"
                                    }"""
                                ),
                                @ExampleObject(
                                        name = "Invalid request - Empty newFileName",
                                        value = """
                                    {
                                        "filepath": "img",
                                        "newFileName": ""
                                    }"""
                                )
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "File successfully renamed",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = "{ \"message\": \"File successfully renamed\" }"
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid input data)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Invalid request - Incorrect filepath",
                                                value = """
                                            {
                                                "filepath": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing filepath",
                                                value = """
                                            {
                                                "filepath": "The path must match the format img, img/png, files/photo/img"
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Missing newFileName",
                                                value = """
                                            {
                                                "newFileName": "Filename cannot be empty."
                                            }"""
                                        ),
                                        @ExampleObject(
                                                name = "Invalid request - Empty newFileName",
                                                value = """
                                            {
                                                "newFileName": "Filename cannot be empty."
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
public @interface RenameFileApiDocs {
}
