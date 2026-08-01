package com.nightluxe.core.repository;

import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.entity.Category;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.enums.AdStatus;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, JpaSpecificationExecutor<Advertisement> {


    List<Advertisement> findByPrice(Integer price);

    List<Advertisement> findByTitleContainingIgnoreCase(String title);

    List<Advertisement> findByStatusAndCategory(AdStatus status, Category category);

    List<Advertisement> findByUser(User user);

    List<Advertisement> findByLocationContainingIgnoreCase(String location); //for example a city search

    List<Advertisement> findByUserId(Long userId);

    Page<Advertisement> findByStatusAndCategory(AdStatus adStatus, Category category, Pageable pageable);

    @Modifying
    @Query("UPDATE Advertisement a SET a.promotedUntil = null, a.isHighlighted = false WHERE a.promotedUntil < :now")
    int clearExpiredPromotions(@Param("now") Instant now);

    // find all ads for an specified user
    Page<Advertisement> findByUserId(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Advertisement a SET a.viewCount = a.viewCount + 1 WHERE a.id = :adId ")
    void incrementViewCount(@Param("adId") Long adId);


    @Modifying
    @Query("UPDATE Advertisement a SET a.phoneRevealsCount = a.phoneRevealsCount + 1 WHERE a.id = :adId")
    void incrementPhoneRevealsCount(@Param("adId") Long adId);


}

