package com.vladislavlevchik.cloud_file_storage.docs.folders;

import com.vladislavlevchik.cloud_file_storage.dto.request.folder.FolderChangeColorRequestDto;
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
        summary = "Change folder color",
        description = "Change the color of an existing folder belonging to an authenticated user.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Folder color change request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FolderChangeColorRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = "{ \"newColor\": \"#ffffff\" }"),
                                @ExampleObject(name = "Empty Request", value = "{}"),
                                @ExampleObject(name = "Invalid HEX Code", value = "{ \"newColor\": \"#ffffff1\" }")
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Folder color successfully changed",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Folder color successful change"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid color format or empty color)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(name = "Empty Request", value = """
                                                    {
                                                        "newColor": "File color cannot be empty."
                                                    }
                                                    """),
                                        @ExampleObject(name = "Invalid HEX Code", value = """
                                                    {
                                                        "newColor": "Color must be a valid HEX code (e.g. #ffffff or #fff)."
                                                    }
                                                    """),
                                        @ExampleObject(name = "Empty Color", value = """
                                                    {
                                                        "newColor": "File color cannot be empty."
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
                )
        }
)
public @interface ChangeFolderColorApiDocs {
}
