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
        summary = "Search files in the 'deleted' folder by name",
        description = "Searches for all files within the 'deleted' folder that contain the provided substring in their filenames. If no substring is provided or if it's an empty string, an error will be returned.",
        parameters = {
                @Parameter(
                        name = "fileName",
                        description = "The substring to search for within filenames. If omitted or empty, an error will be returned.",
                        required = true,
                        example = "s"
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
                                                name = "Files containing 's'",
                                                value = """
                                                            [
                                                                {
                                                                    "filename": "Frame Home-art-scale-2_00x-gigapixel.png",
                                                                    "filePath": "img/png",
                                                                    "size": "1,64MB",
                                                                    "lastModified": {
                                                                        "day": "2024-08-17",
                                                                        "time": "15:39:44.092"
                                                                    },
                                                                    "color": null,
                                                                    "customFolderName": null
                                                                }
                                                            ]"""
                                        ),
                                        @ExampleObject(
                                                name = "Files containing 'a'",
                                                value = """
                                                            [
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
                                                                 },
                                                                 {
                                                                     "filename": "Frame Home-art-scale-2_00x-gigapixel.png",
                                                                     "filePath": "img/png",
                                                                     "size": "1,64MB",
                                                                     "lastModified": {
                                                                         "day": "2024-08-17",
                                                                         "time": "15:39:44.092"
                                                                     },
                                                                     "color": null,
                                                                     "customFolderName": null
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
public @interface SearchFilesInDeletedApiDocs {
}
