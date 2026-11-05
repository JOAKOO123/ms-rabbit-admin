package com.hotelboutique.rabbitadmin.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrearColaRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void nombreVacioNoEsValido() {
        assertFalse(validator.validate(new CrearColaRequest("", true)).isEmpty());
        assertFalse(validator.validate(new CrearColaRequest("   ", true)).isEmpty());
    }

    @Test
    void nombreValidoPasaLaValidacion() {
        assertTrue(validator.validate(new CrearColaRequest("q.demo", null)).isEmpty());
    }

    @Test
    void durableNuloQuedaEnTrue() {
        assertTrue(new CrearColaRequest("q.demo", null).durable());
    }
}
