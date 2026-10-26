package com.hotelboutique.rabbitadmin.controller;

import com.hotelboutique.rabbitadmin.dto.CrearBindingRequest;
import com.hotelboutique.rabbitadmin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bindings")
public class BindingsController {

    private final RabbitAdminService rabbitAdminService;

    public BindingsController(RabbitAdminService rabbitAdminService) {
        this.rabbitAdminService = rabbitAdminService;
    }

    @PostMapping
    public ResponseEntity<Void> crearBinding(@Valid @RequestBody CrearBindingRequest request) {
        rabbitAdminService.crearBinding(request.exchange(), request.cola(), request.routingKey());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarBinding(
            @RequestParam String exchange,
            @RequestParam String cola,
            @RequestParam String routingKey) {
        if (!rabbitAdminService.eliminarBinding(exchange, cola, routingKey)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
