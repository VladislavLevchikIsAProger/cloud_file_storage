package com.vladislavlevchik.cloud_file_storage.docs.users;

import com.vladislavlevchik.cloud_file_storage.dto.request.user.UserRegisterRequestDto;
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
        summary = "Register a new user",
        description = "Save the user to the users database",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "User registration request payload",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserRegisterRequestDto.class),
                        examples = {
                                @ExampleObject(name = "Valid Request", value = "{ \"username\": \"validUser\", \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Missing Password", value = "{ \"username\": \"validUser\" }"),
                                @ExampleObject(name = "Missing Username", value = "{ \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Empty Username", value = "{ \"username\": \"\", \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Empty Password", value = "{ \"username\": \"validUser\", \"password\": \"\" }"),
                                @ExampleObject(name = "Too short Username", value = "{ \"username\": \"v\", \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Too short Password", value = "{ \"username\": \"validUser\", \"password\": \"va\" }"),
                                @ExampleObject(name = "Too long Username", value = "{ \"username\": \"validUservalidUservalidUser\", \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Too long Password", value = "{ \"username\": \"validUser\", \"password\": \"validPasswordvalidPasswordvalidPassword\" }"),
                                @ExampleObject(name = "Invalid Pattern Username", value = "{ \"username\": \"validUser.\", \"password\": \"validPassword\" }"),
                                @ExampleObject(name = "Invalid Pattern Password", value = "{ \"username\": \"validUser\", \"password\": \"validPassword.\" }"),
                        }
                )
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User successfully registered",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = "{ \"message\": \"User validUser successfully registered!\" }")
                        )
                ),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflict (e.g., user already exists)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = @ExampleObject(value = "{ \"message\": \"User already exists\" }")
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request (e.g., invalid request data)",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = MessageResponseDto.class),
                                examples = {
                                        @ExampleObject(name = "Missing Username", value = "{ \"username\": \"Username is mandatory\" }"),
                                        @ExampleObject(name = "Missing Password", value = "{ \"password\": \"Password is mandatory\" }"),
                                        @ExampleObject(name = "Empty Username", value = "{ \"username\": \"Username cannot be empty\" }"),
                                        @ExampleObject(name = "Empty Password", value = "{ \"password\": \"Password cannot be empty\" }"),
                                        @ExampleObject(name = "Too short Username", value = "{ \"username\": \"Username must be between 2 and 20 characters\" }"),
                                        @ExampleObject(name = "Too short Password", value = "{ \"password\": \"Username must be between 2 and 20 characters\" }"),
                                        @ExampleObject(name = "Too long Username", value = "{ \"username\": \"Username cannot be more than 20 characters long\" }"),
                                        @ExampleObject(name = "Too long Password", value = "{ \"password\": \"Password must be between 3 and 20 characters\" }"),
                                        @ExampleObject(name = "Invalid Pattern Username", value = "{ \"username\": \"Username can only contain letters and numbers\" }"),
                                        @ExampleObject(name = "Invalid Pattern Password", value = "{ \"password\": \"Password can only contain letters and numbers\" }"),
                                }
                        )
                )
        }
)
public @interface RegisterUserOperation {
}
