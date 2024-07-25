package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.MessageDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserLoginRequestDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRegisterRequestDto;
import com.vladislavlevchik.cloud_file_storage.service.AuthService;
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
    public ResponseEntity<?> login(@RequestBody @Validated UserLoginRequestDto userLoginRequestDto) {
        service.signIn(userLoginRequestDto);

        return ResponseEntity.ok(MessageDto.builder()
                .message("User " + userLoginRequestDto.getUsername() + " logged in successfully!")
                .build()
        );
    }

    //TODO мб вернуть message а не объект
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserRegisterRequestDto userRegisterRequestDto) {

        return ResponseEntity.ok(service.registerUser(userRegisterRequestDto));
    }

}
