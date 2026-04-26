package com.nightluxe.dto.response;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class UserResponseDTO {

    private Long id;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private Integer creditBalance;
    private Boolean isVerified;
    private Instant createdAt;
}
