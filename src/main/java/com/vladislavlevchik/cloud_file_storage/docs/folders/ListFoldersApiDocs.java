package com.vladislavlevchik.cloud_file_storage.docs.folders;

import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.folder.FolderResponseDto;
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
        summary = "List all folders",
        description = "Retrieve a list of all folders belonging to the authenticated user.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of folders successfully retrieved",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = FolderResponseDto.class),
                                examples = @ExampleObject(value = """
                                    [
                                        {
                                            "name": "img1",
                                            "color": "#ffffff",
                                            "size": "99,46MB",
                                            "filesNumber": "2"
                                        },
                                        {
                                            "name": "Me",
                                            "color": "#D23434",
                                            "size": "22,82MB",
                                            "filesNumber": "6"
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
public @interface ListFoldersApiDocs {
}
