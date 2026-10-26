package com.hotelboutique.rabbitadmin.dto;

import jakarta.validation.constraints.NotBlank;

public record CrearColaRequest(
        @NotBlank String nombre,
        Boolean durable
) {

    public CrearColaRequest {
        if (durable == null) {
            durable = true;
        }
    }
}
