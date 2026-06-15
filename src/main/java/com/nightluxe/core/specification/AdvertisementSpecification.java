package com.nightluxe.core.specification;

import com.nightluxe.core.dto.request.AdSearchCriteriaDTO;
import com.nightluxe.core.entity.Advertisement;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementSpecification {


    public static Specification<Advertisement> getAdvertisementsByCriteria(AdSearchCriteriaDTO criteria){
        return (root, query, criteriaBuilder) -> {

            // in this arraylist we add all our conditions for search
            List<Predicate> predicates = new ArrayList<>();

            //1. search based on keywords
            if (criteria.keyword() != null && !criteria.keyword().trim().isEmpty()) {
                String pattern = "%" + criteria.keyword().toLowerCase() + "%";

                //in-case sentitive search
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern);
                Predicate descPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern);

                // we combine them to contain title or description
                predicates.add(criteriaBuilder.or(titlePredicate, descPredicate));
            }

            //filter by category
            if (criteria.categoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), criteria.categoryId()));
            }

            // filter by min price
            if (criteria.minPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.minPrice()));
            }

            //filter by max price
            if (criteria.maxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.maxPrice()));
            }

            //filter by location
            if (criteria.location() != null && !criteria.location().trim().isEmpty()) {
                String locationPattern = "%" + criteria.location().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), locationPattern));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
