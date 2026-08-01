package com.nightluxe.core.controller;

import com.nightluxe.core.dto.request.UpdateProfileRequestDTO;
import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.dto.response.UserResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.service.AdvertisementService;
import com.nightluxe.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final AdvertisementService advertisementService;

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

    @GetMapping("/me/ads")
    public ResponseEntity<Page<AdvertisementResponseDTO>> getMyAds(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(size = 10) Pageable pageable){

        Page<AdvertisementResponseDTO> response = advertisementService.getMyAdvertisements(currentUser, pageable);
        return ResponseEntity.ok(response);
    }

}
