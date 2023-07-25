package com.br.equaly.messenger.model.dto.recovery;

import jakarta.validation.constraints.NotBlank;

public record RecoveryRequestDTO(

        @NotBlank
        String token,

        @NotBlank
        String messageType,

        @NotBlank
        String email
) {
}
