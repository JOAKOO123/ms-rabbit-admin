package com.hotelboutique.rabbitadmin.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.AmqpException;
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

    public void crearExchange(String nombre, String tipo, boolean durable) {
        if ("direct".equals(tipo)) {
            rabbitAdmin.declareExchange(new DirectExchange(nombre, durable, false));
            return;
        }
        rabbitAdmin.declareExchange(new TopicExchange(nombre, durable, false));
    }

    public boolean eliminarExchange(String nombre) {
        try {
            rabbitAdmin.getRabbitTemplate().execute(channel -> {
                channel.exchangeDeclarePassive(nombre);
                return null;
            });
        } catch (AmqpException ex) {
            return false;
        }
        return rabbitAdmin.deleteExchange(nombre);
    }
}
