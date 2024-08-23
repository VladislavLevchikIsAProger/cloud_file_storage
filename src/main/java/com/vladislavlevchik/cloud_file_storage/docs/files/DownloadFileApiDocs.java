package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileAndFolderResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Download a file",
        description = "Downloads a file from the server for an authenticated user. The file is returned as an octet-stream, allowing the user to save it locally.",
        parameters = {
                @Parameter(
                        name = "fileName",
                        description = "The path to the file to be downloaded, relative to the user's root directory.",
                        required = true,
                        example = "img/exe1/file.txt"
                )
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "File successfully downloaded",
                        content = @Content(
                                mediaType = "application/octet-stream",
                                schema = @Schema(type = "string", format = "binary"),
                                examples = @ExampleObject(name = "File Download Response", value = "(binary file content)")
                        ),
                        headers = {
                                @Header(
                                        name = HttpHeaders.CONTENT_DISPOSITION,
                                        description = "Specifies that the file is an attachment and should be downloaded",
                                        schema = @Schema(type = "string", example = "attachment; filename=\"file.txt\"")
                                )
                        }
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., fileName is missing or invalid)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = @ExampleObject(value = """
                                        {
                                            "message": "The path must match the format img, img/png, files/photo/img"
                                        }
                                        """)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized (user not authenticated)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                        {
                                            "authenticated": "false"
                                        }
                                        """)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "File not found",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                        {
                                            "message": "File user-Vlad/Me/FM24.zip1 not found"
                                        }
                                        """)
                        )
                )
        }
)
public @interface DownloadFileApiDocs {
}
