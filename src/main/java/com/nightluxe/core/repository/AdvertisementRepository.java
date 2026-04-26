package com.nightluxe.core.repository;

import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.entity.Category;
import com.nightluxe.core.entity.User;
import com.nightluxe.enums.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {


    List<Advertisement> findByPrice(Integer price);
    List<Advertisement> findByTitleContainingIgnoreCase(String title);
    List<Advertisement> findByStatusAndCategory(AdStatus status, Category category);
    List<Advertisement> findByUser(User user);
    List<Advertisement> findByLocationContainingIgnoreCase(String location); //for example a city search
    List<Advertisement> findByUserId(Long userId);


    Page<Advertisement> findByStatusAndCategory(AdStatus adStatus, Category category, Pageable pageable);
}
