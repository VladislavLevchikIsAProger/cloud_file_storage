package com.vladislavlevchik.cloud_file_storage.docs.folders;

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

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "Delete a folder",
        description = "Deletes an existing folder belonging to an authenticated user. Also, all previously deleted files that are in the 'deleted' folder will automatically be moved to the root of the 'deleted' folder",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Folder successfully deleted",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Folder successful deleted"
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
                )
        }
)
public @interface DeleteFolderApiDocs {
}
