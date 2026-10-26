package com.hotelboutique.rabbitadmin.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final RabbitAdmin rabbitAdmin;

    public RabbitAdminService(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    public void crearCola(String nombre, boolean durable) {
        rabbitAdmin.declareQueue(new Queue(nombre, durable));
    }

    public boolean eliminarCola(String nombre) {
        if (rabbitAdmin.getQueueProperties(nombre) == null) {
            return false;
        }
        return rabbitAdmin.deleteQueue(nombre);
    }
}
