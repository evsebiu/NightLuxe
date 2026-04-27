package com.nightluxe.core.controller;

import com.nightluxe.core.dto.request.LoginRequestDTO;
import com.nightluxe.core.dto.response.TokenResponseDTO;
import com.nightluxe.core.service.AuthService;
import com.nightluxe.core.dto.request.RegisterRequestDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO requestDTO){
        return ResponseEntity.ok(authService.register(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login (@RequestBody @Valid LoginRequestDTO request){
        return  ResponseEntity.ok(authService.login(request));
    }
}
