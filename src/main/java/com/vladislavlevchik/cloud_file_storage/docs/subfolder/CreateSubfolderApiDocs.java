package com.vladislavlevchik.cloud_file_storage.docs.subfolder;

import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRequestDto;
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
        summary = "Create a subfolder",
        description = "Creates a new subfolder in the specified folder path for an authenticated user.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Subfolder creation request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SubFolderRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request 1", value = """
                                        {
                                            "folderPath": "Me",
                                            "name": "2023"
                                        }
                                        """),
                                @ExampleObject(name = "Valid Request 2", value = """
                                        {
                                            "folderPath": "Me/2023",
                                            "name": "pic"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Invalid Path", value = """
                                        {
                                            "folderPath": "Me/",
                                            "name": "2023"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Path", value = """
                                        {
                                            "name": "2023"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Name", value = """
                                        {
                                            "folderPath": "Me"
                                        }
                                        """)
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Subfolder successfully created",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Subfolder successfully created"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid folder path or missing subfolder name)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(name = "Invalid Path Response", value = """
                                            {
                                                "folderPath": "The path must match the format img/png, files/photo/img"
                                            }
                                            """),
                                        @ExampleObject(name = "Missing Name Response", value = """
                                            {
                                                "name": "Subfolder name cannot be empty."
                                            }
                                            """)
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized (user not authenticated)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = "{ \"authenticated\": \"false\" }")
                        )
                )
        }
)
public @interface CreateSubfolderApiDocs {
}
