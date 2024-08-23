package com.vladislavlevchik.cloud_file_storage.docs.folders;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.folder.FolderForMoveResponseDto;
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
        summary = "List folders available for moving files",
        description = "Retrieve a list of folders to which files can be moved, belonging to the authenticated user.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of folders successfully retrieved",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = FolderForMoveResponseDto.class),
                                examples = @ExampleObject(value = """
                                    [
                                        {
                                            "name": "img1",
                                            "color": "#ffffff"
                                        },
                                        {
                                            "name": "Me",
                                            "color": "#D23434"
                                        }
                                    ]
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
                )
        }
)
public @interface ListFoldersForMoveApiDocs {
}
