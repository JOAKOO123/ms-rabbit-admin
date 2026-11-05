package com.hotelboutique.rabbitadmin.controller;

import com.hotelboutique.rabbitadmin.dto.CrearExchangeRequest;
import com.hotelboutique.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchanges")
public class ExchangesController {

    private final RabbitAdminService rabbitAdminService;

    public ExchangesController(RabbitAdminService rabbitAdminService) {
        this.rabbitAdminService = rabbitAdminService;
    }

    @PostMapping
    public ResponseEntity<Void> crearExchange(@Valid @RequestBody CrearExchangeRequest request) {
        rabbitAdminService.crearExchange(request.nombre(), request.tipo(), request.durable());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> eliminar(@PathVariable String name) {
        if (!rabbitAdminService.eliminarExchange(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
