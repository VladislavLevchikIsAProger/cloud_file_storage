package com.vladislavlevchik.cloud_file_storage.controller;

import com.vladislavlevchik.cloud_file_storage.dto.MessageDto;
import com.vladislavlevchik.cloud_file_storage.dto.UserRequestDto;
import com.vladislavlevchik.cloud_file_storage.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    //TODO не забыть удалить
    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        service.signIn(userRequestDto);

        return ResponseEntity.ok(MessageDto.builder()
                .message("User " + userRequestDto.getUsername() + " logged in successfully!")
                .build()
        );
    }

    //TODO мб вернуть message а не объект
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(service.registerUser(userRequestDto));
    }

}
