package com.nightluxe.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportRequestDTO(
        @NotBlank(message = "Raport reason it's necessary")
        @Size(max = 500, message = "Reason cannot exceed 500 characters.")
        String reason
) {
}
