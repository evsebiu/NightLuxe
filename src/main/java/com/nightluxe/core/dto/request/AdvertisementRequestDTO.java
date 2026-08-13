package com.nightluxe.core.dto.request;

import com.nightluxe.core.enums.UserAdDashboard;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AdvertisementRequestDTO(
        @NotBlank(message = "Title is required.")
        @Size(min = 5, max = 100, message = "Title must have between 5 to 100 characters.")
        String title,

        @NotBlank(message = "Description is required")
        @Size(min = 20, max = 5000, message = "Description must be detailed, between 20 to 5.000 characters.")
        String description,

        @NotNull(message = "Price is required.")
        @PositiveOrZero (message = "Price can't be negative.")
        Integer price,

        @NotBlank(message = "Location is required.")
        String location,

        @NotNull(message = "You must select a category for ad.")
        Long categoryId,

        @NotBlank(message = "Phone number is required.")
        String phoneNumber

) {

}
