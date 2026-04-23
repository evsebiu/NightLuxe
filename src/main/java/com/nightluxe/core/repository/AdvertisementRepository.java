package com.nightluxe.core.repository;

import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.entity.Category;
import com.nightluxe.core.entity.User;
import com.nightluxe.enums.AdStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {


    List<Advertisement> findByPrice(Integer price);
    List<Advertisement> findByTitle(String title);
    List<Advertisement> findByStatusAndCategory(AdStatus status, Category category);
    List<Advertisement> findByUserId(User user);
    List<Advertisement> findByLocation(String location); //for example a city search ?

}
