package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.AdvertisementRequestDTO;
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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public AdvertisementResponseDTO uploadImages(Long adId, List<MultipartFile> files){
         // 1. we search for ad that photos belong
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(()-> new BadRequestException("Ad doesn't exist."));

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

}
