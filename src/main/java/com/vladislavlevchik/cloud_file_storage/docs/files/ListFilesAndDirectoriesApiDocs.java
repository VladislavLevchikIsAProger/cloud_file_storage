package com.vladislavlevchik.cloud_file_storage.docs.files;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.file.FileAndFolderResponseDto;
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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "List files and directories",
        description = "Gets a list of files and subfolders on the specified path. Each subfolder displays the last change of the file in it and its size",
        parameters = {
                @Parameter(
                        name = "path",
                        description = "Path to the folder in which we want to get the list of subfolders and files.",
                        schema = @Schema(type = "string"),
                        examples = {
                                @ExampleObject(name = "Valid Paths", value = "img"),
                                @ExampleObject(name = "Valid Paths1", value = "img/png"),
                                @ExampleObject(name = "Valid Paths2", value = "files/png/photo"),
                                @ExampleObject(name = "Invalid Paths", value = "/img/png"),
                                @ExampleObject(name = "Invalid Paths1", value = "img/png/"),
                                @ExampleObject(name = "Invalid Paths2", value = "img/"),
                                @ExampleObject(name = "Invalid Paths3", value = ""),
                        }
                )
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved list of files and directories",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = FileAndFolderResponseDto.class),
                                examples = @ExampleObject(
                                        value = """
                                            {
                                                 "folders": [
                                                     {
                                                         "name": "png",
                                                         "size": "1,64MB",
                                                         "lastModified": {
                                                             "day": "2024-08-15",
                                                             "time": "11:45:59.390"
                                                         }
                                                     }
                                                 ],
                                                 "files": [
                                                     {
                                                         "filename": "MediaGet_id288309ids1s.exe",
                                                         "filePath": "img",
                                                         "size": "4,61MB",
                                                         "lastModified": {
                                                             "day": "2024-08-15",
                                                             "time": "11:45:59.366"
                                                         },
                                                         "color": null,
                                                         "customFolderName": null
                                                     }
                                                 ]
                                             }"""
                                )
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid path or missing parameter)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = {
                                        @ExampleObject(
                                                name = "Invalid Path Format",
                                                value = "{ \"message\": \"The path must match the format img, img/png, files/photo/img\" }"
                                        ),
                                        @ExampleObject(
                                                name = "Missing Parameter",
                                                value = "{ \"message\": \"You forgot to pass the parameter\" }"
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
public @interface ListFilesAndDirectoriesApiDocs {
}
