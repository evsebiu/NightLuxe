package com.nightluxe.core.dto.request;

import com.nightluxe.core.enums.UserAdDashboard;
import jakarta.validation.constraints.NotNull;

public record AdvertisementVisibilityRequestDTO(
        @NotNull(message = "Visibility status is required.")
        UserAdDashboard status
) {
}
