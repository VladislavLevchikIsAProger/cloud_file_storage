package com.vladislavlevchik.cloud_file_storage.docs.subfolder;

import com.vladislavlevchik.cloud_file_storage.dto.request.subfolder.SubFolderRenameRequestDto;
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
        summary = "Rename a subfolder",
        description = "Renames an existing subfolder in the specified folder path for an authenticated user. Also changes all files that are in the 'deleted' folder from the changed subfolder will automatically change to the new path.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Subfolder rename request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SubFolderRenameRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = """
                                        {
                                            "oldName": "img/exe",
                                            "newName": "img/exe1"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing newName", value = """
                                        {
                                            "oldName": "img/exe"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing oldName", value = """
                                        {
                                            "newName": "img/exe1"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Invalid oldName", value = """
                                        {
                                            "oldName": "img/exe12/",
                                            "newName": "img/exe1"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Invalid newName", value = """
                                        {
                                            "oldName": "img/exe",
                                            "newName": "/img/exe1/"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Both invalid paths", value = """
                                        {
                                            "newName": "The path must match the format img/png, files/photo/img",
                                            "oldName": "The path must match the format img/png, files/photo/img"
                                        }
                                        """)
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Subfolder successfully renamed",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                        {
                                            "message": "Subfolder successful renamed"
                                        }
                                        """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid folder path or missing subfolder names)",
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
public @interface RenameSubfolderApiDocs {
}
