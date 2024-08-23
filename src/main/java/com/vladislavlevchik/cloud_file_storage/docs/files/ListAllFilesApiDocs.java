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
        summary = "List all files",
        description = "Gets a list of all files (except those in deleted) for the current authenticated user.",
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
                                                                "filename": "ChromeSetup.exe",
                                                                "filePath": "",
                                                                "size": "1,31MB",
                                                                "lastModified": {
                                                                    "day": "2024-08-15",
                                                                    "time": "11:42:08.634"
                                                                },
                                                                "color": null,
                                                                "customFolderName": ""
                                                            },
                                                            {
                                                                "filename": "EPAM-Code-Instructions.doc",
                                                                "filePath": "",
                                                                "size": "49,00KB",
                                                                "lastModified": {
                                                                    "day": "2024-08-14",
                                                                    "time": "10:19:18.177"
                                                                },
                                                                "color": null,
                                                                "customFolderName": ""
                                                            },
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
                                                            },
                                                            {
                                                                "filename": "SteamSetup1.exe",
                                                                "filePath": "files/img",
                                                                "size": "2,27MB",
                                                                "lastModified": {
                                                                    "day": "2024-08-14",
                                                                    "time": "21:40:33.512"
                                                                },
                                                                "color": null,
                                                                "customFolderName": "files"
                                                            },
                                                            {
                                                                "filename": "hebirnate.png",
                                                                "filePath": "",
                                                                "size": "3,55KB",
                                                                "lastModified": {
                                                                    "day": "2024-08-12",
                                                                    "time": "12:10:17.646"
                                                                },
                                                                "color": null,
                                                                "customFolderName": ""
                                                            },
                                                            {
                                                                "filename": "html.png",
                                                                "filePath": "",
                                                                "size": "3,74KB",
                                                                "lastModified": {
                                                                    "day": "2024-08-12",
                                                                    "time": "12:10:17.658"
                                                                },
                                                                "color": null,
                                                                "customFolderName": ""
                                                            },
                                                            {
                                                                "filename": "MediaGet_id288309ids1s.exe",
                                                                "filePath": "img",
                                                                "size": "4,61MB",
                                                                "lastModified": {
                                                                    "day": "2024-08-15",
                                                                    "time": "11:45:59.366"
                                                                },
                                                                "color": "#ffffff",
                                                                "customFolderName": "img"
                                                            },
                                                            {
                                                                "filename": "Frame Home-art-scale-2_00x-gigapixel.png",
                                                                "filePath": "img/png",
                                                                "size": "1,64MB",
                                                                "lastModified": {
                                                                    "day": "2024-08-15",
                                                                    "time": "11:45:59.390"
                                                                },
                                                                "color": "#ffffff",
                                                                "customFolderName": "img"
                                                            },
                                                            {
                                                                "filename": "sql.txt",
                                                                "filePath": "",
                                                                "size": "2,30KB",
                                                                "lastModified": {
                                                                    "day": "2024-08-15",
                                                                    "time": "11:42:08.692"
                                                                },
                                                                "color": null,
                                                                "customFolderName": ""
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
public @interface ListAllFilesApiDocs {
}
