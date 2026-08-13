package com.nightluxe.core.controller;


import com.nightluxe.core.dto.request.AdvertisementVisibilityRequestDTO;
import com.nightluxe.core.dto.request.AdvertisementRequestDTO;
import com.nightluxe.core.dto.request.PromotionRequestDTO;
import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.service.AdvertisementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nightluxe.core.dto.request.AdSearchCriteriaDTO;


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
            @RequestParam("files")List<MultipartFile> files,
            @AuthenticationPrincipal User currentUser){

        AdvertisementResponseDTO response = advertisementService.uploadImages(id, files, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdvertisement(@PathVariable Long id,
                                                    @AuthenticationPrincipal User currentUser){
        advertisementService.deleteAdvertisement(id, currentUser);

        return ResponseEntity.noContent().build();
    }

    // secured endpoint - buying a package of promotion
    @PostMapping("/{id}/promote")
    public ResponseEntity<AdvertisementResponseDTO> promoteAd(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequestDTO request,
            @AuthenticationPrincipal User currentUser){

        AdvertisementResponseDTO updatedAd = advertisementService.promoteAd(id,request,currentUser);

        return ResponseEntity.ok(updatedAd);
    }

    // public endpoints - search and listings ads

    @GetMapping
    public ResponseEntity<Page<AdvertisementResponseDTO>> getAdvertisements(
            AdSearchCriteriaDTO criteria,
            @PageableDefault(size = 20) Pageable clientPageable){
        /*premium architecture
        we ignore sorting sent by client for business reasons
        override rules by sorting like:
        1. promotedUntil DESC NULLS LAST -> Top ads stays up. Those who doesn't have TOP UP(NULL) drops.
        2. createdAt DESC -> Afte what TOP ADS ends, we sort the others by most recents
         */

        Sort premiumBusinessSort = Sort.by(
                Sort.Order.desc("promotedUntil").nullsLast(),
                Sort.Order.desc("createdAt")
        );

        // we create a new Pageable, mentaining only the bage and size from client, but we apply our rule

        Pageable enforcePageable = PageRequest.of(
                clientPageable.getPageNumber(),
                clientPageable.getPageSize(),
                premiumBusinessSort
        );

        Page<AdvertisementResponseDTO> responseDTO = advertisementService.getAdvertisement(criteria, enforcePageable);

        return ResponseEntity.ok(responseDTO);
    }


    //PUBLIC ENDPOINT
    @GetMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDTO> getAdDetails(@PathVariable Long id){
        AdvertisementResponseDTO response = advertisementService.getAdvertisementById(id);

        return ResponseEntity.ok(response);
    }

    // PUBLIC ENDPOINT
    @GetMapping("/{id}/phone")
    public ResponseEntity<String> getPhoneNumber(@PathVariable Long id){
        String phoneNumber = advertisementService.revealPhoneNumber(id);
        return ResponseEntity.ok(phoneNumber);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdvertisementResponseDTO> updateDetails(@Valid @RequestBody AdvertisementRequestDTO request,
                                                                  @AuthenticationPrincipal User currentUser,
                                                                  @PathVariable("id") Long id){

        AdvertisementResponseDTO updateAdvertisement = advertisementService.updateAdvertisement(id, currentUser, request);
        return ResponseEntity.ok(updateAdvertisement);
    }

    @PatchMapping("/{id}/visibility")
    public ResponseEntity<AdvertisementResponseDTO> changeVisibility(@PathVariable("id") Long id,
                                                                     @Valid @RequestBody AdvertisementVisibilityRequestDTO request,
                                                                     @AuthenticationPrincipal User currentUser){

        AdvertisementResponseDTO changeVisibility = advertisementService.changeVisibility(id, currentUser, request.status());

        return ResponseEntity.ok(changeVisibility);

    }
}
