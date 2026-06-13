package com.nightluxe.core.repository;

import com.nightluxe.core.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Optional<Category> findByName(String name);

    Optional<Category> findBySlug(String slug);

    @Query("SELECT DISTINCT c from Category c LEFT JOIN FETCH c.subCategories " +
    "WHERE c.parentCategory IS NULL AND c.isActive = true " +
    "ORDER BY c.displayOrder ASC")
    List<Category> findActiveRootCategoriesWithSubcategories();

}
