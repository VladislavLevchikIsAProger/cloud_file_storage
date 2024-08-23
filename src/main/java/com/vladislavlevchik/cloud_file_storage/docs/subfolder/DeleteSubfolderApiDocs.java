package com.vladislavlevchik.cloud_file_storage.docs.subfolder;

import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderDeleteRequestDto;
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
        summary = "Delete a subfolder",
        description = "Deletes an existing subfolder in the specified folder path for an authenticated user. Also all files in deleted from this subfolder are automatically moved to the root",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Subfolder deletion request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SubFolderDeleteRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = """
                                        {
                                            "folderPath": "img/exe1"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Invalid Path", value = """
                                        {
                                            "folderPath": "img/exe/"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Path", value = """
                                        {
                                        }
                                        """)
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Subfolder successfully deleted",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Subfolder successful deleted"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid folder path or missing subfolder path)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(name = "Invalid Path Response", value = """
                                            {
                                                "folderPath": "The path must match the format img/png, files/photo/img"
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
public @interface DeleteSubfolderApiDocs {
}
