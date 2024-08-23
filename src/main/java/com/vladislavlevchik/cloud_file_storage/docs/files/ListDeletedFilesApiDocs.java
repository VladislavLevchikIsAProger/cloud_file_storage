package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileResponseDto;
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
        summary = "List deleted files",
        description = "Gets a list of all files in the deleted folder for the current authenticated user.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved list of files",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = FileResponseDto.class),
                                examples = @ExampleObject(
                                        value = """
                                                    [
                                                        {
                                                            "filename": "VSCodeUserSetup-x64-1.88.0.exe",
                                                            "filePath": "img",
                                                            "size": "94,85MB",
                                                            "lastModified": {
                                                                "day": "2024-08-15",
                                                                "time": "11:45:59.655"
                                                            },
                                                            "color": null,
                                                            "customFolderName": null
                                                        },
                                                        {
                                                            "filename": "Wallpaper (2).jpg",
                                                            "filePath": "img",
                                                            "size": "464,59KB",
                                                            "lastModified": {
                                                                "day": "2024-08-15",
                                                                "time": "11:45:59.675"
                                                            },
                                                            "color": null,
                                                            "customFolderName": null
                                                        }
                                                    ]"""
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
public @interface ListDeletedFilesApiDocs {
}
