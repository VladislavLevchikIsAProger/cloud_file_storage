package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.docs.auth.GetStatusApiDocs;
import com.vladislavlevchik.cloud_file_storage.docs.auth.LoginApiDocs;
import com.vladislavlevchik.cloud_file_storage.dto.request.user.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.response.MessageResponseDto;
import com.vladislavlevchik.cloud_file_storage.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @LoginApiDocs
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto user, HttpServletRequest request) {
        authService.signIn(user, request);

        return ResponseEntity.ok(MessageResponseDto.builder()
                .message(user.getUsername())
                .build()
        );
    }

    @GetStatusApiDocs
    @GetMapping("/me")
    public ResponseEntity<?> getAuthStatus() {

        return ResponseEntity.ok(authService.checkStatus());
    }
}