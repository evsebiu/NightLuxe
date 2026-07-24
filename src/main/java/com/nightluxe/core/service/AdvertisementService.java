package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.AdvertisementRequestDTO;
import com.nightluxe.core.dto.request.PromotionRequestDTO;
import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.entity.AdImage;
import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.entity.Category;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.exceptions.BadRequestException;
import com.nightluxe.core.mapper.AdvertisementMapper;
import com.nightluxe.core.repository.AdImageRepository;
import com.nightluxe.core.repository.AdvertisementRepository;
import com.nightluxe.core.repository.CategoryRepository;
import com.nightluxe.core.repository.UserRepository;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.nightluxe.core.dto.request.AdSearchCriteriaDTO;
import com.nightluxe.core.specification.AdvertisementSpecification;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementMapper advertisementMapper;
    private final AdImageRepository adImageRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    //for images
    private final String UPLOAD_DIR = "uploads/";



    @Transactional
    public AdvertisementResponseDTO createAdvertisement(AdvertisementRequestDTO request,
                                                         User currentUser){

        // search for category in db
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(()-> new BadRequestException("Selected category doesn't exist"));


        //create ad entity
        Advertisement ad = new Advertisement();
        ad.setTitle(request.title());
        ad.setDescription(request.description());
        ad.setPrice(request.price());
        ad.setLocation(request.location());

        // relations with db
        ad.setCategory(category);
        ad.setUser(currentUser);

        Advertisement savedAd = advertisementRepository.save(ad);

        return advertisementMapper.toResponseDTO(savedAd);
    }


    @Transactional
    public AdvertisementResponseDTO uploadImages(Long adId,
                                                 List<MultipartFile> files,
                                                 User currentUser){
         // 1. we search for ad that photos belong
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(()-> new BadRequestException("Ad doesn't exist."));


        if (!ad.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Unauthorized action. You can't add images to an add that doesn't belong to you.");
        }
        // 2. check that folder "uploads/" exists on disk
        try{
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e){
            throw new RuntimeException("Cannot create folder for files upload");
        }

        List<AdImage> adImages = new ArrayList<>();

        // 3. procces every image recieved

        for (MultipartFile file : files){
            if (file.isEmpty()) continue;

            // generate unique name
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetLocation = Paths.get(UPLOAD_DIR + uniqueFileName);

            try{
                // save file on disk
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // create entity AdImage for database
                AdImage adImage = new AdImage();
                adImage.setImageUrl("/uploads/"+uniqueFileName);
                adImage.setAdvertisement(ad);

                adImages.add(adImage);
            } catch (IOException e){
                throw new RuntimeException("Error at saving file: " + file.getOriginalFilename());
            }
        }

        if (!adImages.isEmpty()){
            adImageRepository.saveAll(adImages);
            if (ad.getImages() == null){
                ad.setImages(new ArrayList<>());
            }
            ad.getImages().addAll(adImages);
        }
        return advertisementMapper.toResponseDTO(ad);
    }


    @Transactional
    public void deleteAdvertisement(Long adId, User currentUser){
        Advertisement advertisement = advertisementRepository.findById(adId)
                .orElseThrow(()-> new BadRequestException("Ad cannot be found."));

        if (!advertisement.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Warning! Ad doesn't belong to your account.");
        }

        List<AdImage> images = advertisement.getImages();

        if (images != null && !images.isEmpty()){
            for (AdImage image : images){
                String imageUrl = image.getImageUrl();

                if (imageUrl != null && imageUrl.startsWith("/")){
                    String localPathString = imageUrl.substring(1);
                    Path filePath = Paths.get(localPathString);

                    try{
                        Files.deleteIfExists(filePath);
                    } catch (IOException e){
                        System.err.println("Error at deleting file : " + filePath.toString());
                    }
                }
            }
        }

        advertisementRepository.delete(advertisement);
    }

    @Transactional(readOnly = true)
    public Page<AdvertisementResponseDTO> getAdvertisement(AdSearchCriteriaDTO criteria, Pageable pageable){

        // build specification based on sent filters
        Specification<Advertisement> spec = AdvertisementSpecification.getAdvertisementsByCriteria(criteria);

        // interogate db using specification and pageable object
        Page<Advertisement> adPage = advertisementRepository.findAll(spec, pageable);

        return adPage.map(advertisementMapper::toResponseDTO);
    }

    @Transactional
    public AdvertisementResponseDTO promoteAd(Long adId, PromotionRequestDTO request, User currentUser){

        //1. find ad and we search if it belongs to user
        Advertisement ad  = advertisementRepository.findById(adId)
                .orElseThrow(()  -> new RuntimeException("Ad doesn't exist."));

        if (!ad.getUser().getId().equals(currentUser.getId())){
            throw new RuntimeException("Action denied. You can't promote an ad that doesn't belongs to you");
        }

        // 2. define costs
        int cost = 0;
        Instant newPromotedUntil = ad.getPromotedUntil() !=null ? ad.getPromotedUntil() : Instant.now();

        switch (request.promotionType().toUpperCase()){
            case "BUMP":
                cost = 5;
                ad.setCreatedAt(Instant.now());
                break;
            case "TOP_AD_7_DAYS":
                cost= 20;
                ad.setPromotedUntil(newPromotedUntil.plus(7, ChronoUnit.DAYS));
                break;
            case "HIGHLIGHT":
                cost=10;
                ad.setIsHighlighted(true);
            default:
                throw new BadRequestException("Promotion type unavailable");
        }

        // 3. Extract credits ATOMIC from database

        int updatedRows = userRepository.deductCredits(currentUser.getId(), cost);
        if (updatedRows == 0){
            throw new BadRequestException("Not enough funds for this promotion");
        }

        // 4. Save updated on ad
        Advertisement savedAd = advertisementRepository.save(ad);

        return advertisementMapper.toResponseDTO(savedAd);

    }
}
