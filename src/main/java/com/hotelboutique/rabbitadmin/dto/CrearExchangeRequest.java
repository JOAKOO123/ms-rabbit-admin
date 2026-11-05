package com.hotelboutique.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CrearExchangeRequest(
        @NotBlank String nombre,
        @NotBlank @Pattern(regexp = "direct|topic") String tipo,
        Boolean durable
) {

    public CrearExchangeRequest {
        if (durable == null) {
            durable = true;
        }
    }
}
