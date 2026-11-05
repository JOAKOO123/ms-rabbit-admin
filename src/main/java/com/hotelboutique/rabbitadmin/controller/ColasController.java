package com.hotelboutique.rabbitadmin.controller;

import com.hotelboutique.rabbitadmin.dto.CrearColaRequest;
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
@RequestMapping("/queues")
public class ColasController {

    private final RabbitAdminService rabbitAdminService;

    public ColasController(RabbitAdminService rabbitAdminService) {
        this.rabbitAdminService = rabbitAdminService;
    }

    @PostMapping
    public ResponseEntity<Void> crearCola(@Valid @RequestBody CrearColaRequest request) {
        rabbitAdminService.crearCola(request.nombre(), request.durable());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> eliminar(@PathVariable String name) {
        if (!rabbitAdminService.eliminarCola(name)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
