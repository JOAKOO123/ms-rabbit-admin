package com.hotelboutique.rabbitadmin.service;

import org.springframework.amqp.core.Binding;
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

    public void crearBinding(String exchange, String cola, String routingKey) {
        rabbitAdmin.declareBinding(binding(exchange, cola, routingKey));
    }

    public boolean eliminarBinding(String exchange, String cola, String routingKey) {
        if (!exchangeExiste(exchange) || rabbitAdmin.getQueueProperties(cola) == null) {
            return false;
        }
        rabbitAdmin.removeBinding(binding(exchange, cola, routingKey));
        return true;
    }

    private Binding binding(String exchange, String cola, String routingKey) {
        return new Binding(cola, Binding.DestinationType.QUEUE, exchange, routingKey, null);
    }

    private boolean exchangeExiste(String nombre) {
        try {
            rabbitAdmin.getRabbitTemplate().execute(channel -> {
                channel.exchangeDeclarePassive(nombre);
                return null;
            });
            return true;
        } catch (AmqpException ex) {
            return false;
        }
    }
}
