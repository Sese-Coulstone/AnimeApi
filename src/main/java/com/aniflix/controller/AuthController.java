package com.aniflix.controller;

import com.aniflix.dto.LoginRequest;
import com.aniflix.dto.RegisterRequest;
import com.aniflix.dto.ResponseDto;
import com.aniflix.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login (@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
