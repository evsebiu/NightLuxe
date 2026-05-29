package com.nightluxe.core.exceptions;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(int status,
                            String error,
                            String message,
                            Instant timestamp) {
}
