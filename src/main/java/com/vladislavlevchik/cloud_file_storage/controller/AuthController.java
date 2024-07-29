package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.MessageDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto user, HttpServletRequest request) {
        service.signIn(user, request);

        return ResponseEntity.ok(MessageDto.builder()
                .message("User " + user.getUsername() + " logged in successfully!")
                .build()
        );
    }

    //TODO мб вернуть message а не объект
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRegisterRequestDto user) {

        return ResponseEntity.ok(service.registerUser(user));
    }

    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = authentication.isAuthenticated();

        Map<String, Object> response = new HashMap<>();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        response.put("authenticated", isAuthenticated);
        response.put("username", userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}