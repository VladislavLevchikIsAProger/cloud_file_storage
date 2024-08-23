package com.vladislavlevchik.cloud_file_storage.docs.auth;

import com.vladislavlevchik.cloud_file_storage.dto.request.user.UserLoginRequestDto;
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
        summary = "User Login",
        description = "Authenticates a user using their username and password.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User login request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserLoginRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = """
                                        {
                                            "username": "user123",
                                            "password": "password123"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Username", value = """
                                        {
                                            "password": "password123"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Password", value = """
                                        {
                                            "username": "user123"
                                        }
                                        """),
                                @ExampleObject(name = "Invalid Request - Missing Both Fields", value = """
                                        {
                                        }
                                        """)
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User successfully authenticated",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "user123"
                                            }
                                            """)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., missing username or password)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = Map.class),
                                examples = {
                                        @ExampleObject(name = "Missing Username Response", value = """
                                            {
                                                "username": "Username is mandatory"
                                            }
                                            """),
                                        @ExampleObject(name = "Missing Password Response", value = """
                                            {
                                                "password": "Password is mandatory"
                                            }
                                            """),
                                        @ExampleObject(name = "Missing Both Fields Response", value = """
                                            {
                                                "username": "Username is mandatory",
                                                "password": "Password is mandatory"
                                            }
                                            """)
                                }
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized (invalid credentials)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = """
                                            {
                                                "message": "Invalid username or password"
                                            }
                                            """)
                        )
                )
        }
)
public @interface LoginApiDocs {
}
