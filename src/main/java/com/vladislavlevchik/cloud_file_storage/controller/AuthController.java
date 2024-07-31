package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.MessageDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto user, HttpServletRequest request) {
        service.signIn(user, request);

        return ResponseEntity.ok(MessageDto.builder()
                .message(user.getUsername())
                .build()
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRegisterRequestDto user) {
        service.registerUser(user);

        return ResponseEntity.ok(MessageDto.builder()
                .message("User " + user.getUsername() + " successfully registered!")
                .build()
        );
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {

        return ResponseEntity.ok(service.checkStatus());
    }
}