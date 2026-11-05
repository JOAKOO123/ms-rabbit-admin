package com.hotelboutique.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public record CrearBindingRequest(
        @NotBlank String exchange,
        @NotBlank String cola,
        @NotBlank String routingKey
) {
}
