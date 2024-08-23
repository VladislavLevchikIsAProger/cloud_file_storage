package com.vladislavlevchik.cloud_file_storage.docs.folders;

import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderRenameRequestDto;
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
        summary = "Rename a folder",
        description = "Rename an existing folder belonging to an authenticated user. Also changes the path of all files deleted from this folder in the 'deleted' folder",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Folder rename request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FolderRenameRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = "{ \"newName\": \"videos\" }"),
                                @ExampleObject(name = "Empty Request", value = "{}"),
                                @ExampleObject(name = "Empty Name", value = "{ \"newName\": \"\" }")
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Folder successfully renamed",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Folder successful renamed"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., empty new folder name)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(name = "Empty Request", value = """
                                                    {
                                                        "newName": "Filename cannot be empty."
                                                    }
                                                    """),
                                        @ExampleObject(name = "Empty Name", value = """
                                                    {
                                                        "newName": "Filename cannot be empty."
                                                    }
                                                    """),
                                        @ExampleObject(name = "Same Names", value = """
                                                    {
                                                        "message": "Same names"
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
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Folder not found",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = """
                                                    {
                                                        "message": "Folder Me1 not found"
                                                    }""")
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Folder renamed already exist",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(
                                        value = """
                                                    {
                                                        "message": "Folder with name=Me already exist"
                                                    }""")
                        )
                )
        }
)
public @interface RenameFolderApiDocs {
}
