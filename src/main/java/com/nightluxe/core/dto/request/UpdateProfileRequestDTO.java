package com.nightluxe.core.dto.request;

import jakarta.validation.constraints.Pattern;

public record UpdateProfileRequestDTO(@Pattern(regexp = "^(\\+356)?(2|7|9)\\d{7}$", message = "Please use a valid format for Malta (+356XXXXXXX)")
                                      String phoneNumber) {
}
