package com.nightluxe.core.controller;


import com.nightluxe.core.dto.request.AdvertisementRequestDTO;
import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.service.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/advertisements")
@RequiredArgsConstructor

public class AdvertisementController {


    private final AdvertisementService advertisementService;


    @PostMapping
    public ResponseEntity<AdvertisementResponseDTO> createAdvertisement(
            @Valid @RequestBody AdvertisementRequestDTO request,
            @AuthenticationPrincipal User currentUser){

        AdvertisementResponseDTO response = advertisementService.createAdvertisement(request,currentUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<AdvertisementResponseDTO> uploadImages(
            @PathVariable Long id,
            @RequestParam("files")List<MultipartFile> files){

        AdvertisementResponseDTO response = advertisementService.uploadImages(id, files);
        return ResponseEntity.ok(response);
    }

}
