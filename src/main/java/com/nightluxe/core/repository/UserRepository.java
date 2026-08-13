package com.nightluxe.core.repository;

import com.nightluxe.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE User u SET u.creditBalance = u.creditBalance - :cost WHERE u.id = :userId AND u.creditBalance >= :cost")
    int deductCredits(@Param("userId") Long userId, @Param("cost") Integer cost);

    @Modifying
    @Query("UPDATE User u SET u.creditBalance = u.creditBalance + :amount WHERE u.id = :userId")
    int addCredits(@Param("userId") Long userId, @Param("amount") Integer amount);
}
