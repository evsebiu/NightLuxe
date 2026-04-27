package com.nightluxe.core.dto.request;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data

public class RegisterRequestDTO {


    @NotBlank(message = "E-mail is required.")
    @Email(message = "E-mail format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    //@Pattern(regexp = "^(\\+356)?(2|7|9)\\d{7}$", message = "Format is invalid for Malta +356)")
    private String phoneNumber;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;
}
