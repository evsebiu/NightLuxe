package com.nightluxe.core.controller;

import com.nightluxe.core.dto.request.UpdateProfileRequestDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import com.nightluxe.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){

        UserResponseDTO profile = userService.getProfileByEmail(userDetails.getUsername());

        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                         @Valid @RequestBody UpdateProfileRequestDTO requestDTO
                                                         ){

        UserResponseDTO updateProfile = userService.updateProfile(userDetails.getUsername(), requestDTO);

        return ResponseEntity.ok(updateProfile);
    }
}
