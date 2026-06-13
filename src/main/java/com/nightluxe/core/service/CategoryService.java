package com.nightluxe.core.service;

import com.nightluxe.core.dto.response.CategoryResponseDTO;
import com.nightluxe.core.entity.Category;
import com.nightluxe.core.exceptions.BadRequestException;
import com.nightluxe.core.mapper.CategoryMapper;
import com.nightluxe.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getActiveCategoryHierarchy(){
        return categoryRepository.findActiveRootCategoriesWithSubcategories()
                .stream()
                .map(categoryMapper::toResponseDTO)
                .toList();
    }


    // TEMPORAL FOR MVP

    @Transactional(readOnly = true)
    public CategoryResponseDTO createCategory (Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new BadRequestException("Category already exists");
        }
        if (category.getSlug() != null) {
            category.setSlug(category.getSlug().toLowerCase().trim().replaceAll(" ", "-"));
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(saved);
    }

    @Transactional
    public void deleteCategory (Long id){
        if (!categoryRepository.existsById(id)){
            throw new BadRequestException("Category doesn't exist");
        }
        categoryRepository.deleteById(id);
    }

}