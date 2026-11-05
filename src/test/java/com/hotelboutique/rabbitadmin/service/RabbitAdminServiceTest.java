package com.hotelboutique.rabbitadmin.service;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.mockito.ArgumentCaptor;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RabbitAdminServiceTest {

    private final RabbitAdmin rabbitAdmin = mock(RabbitAdmin.class);
    private final RabbitAdminService service = new RabbitAdminService(rabbitAdmin);

    @Test
    void crearColaDeclaraLaColaConSuNombre() {
        service.crearCola("q.demo", true);

        ArgumentCaptor<Queue> captor = ArgumentCaptor.forClass(Queue.class);
        verify(rabbitAdmin).declareQueue(captor.capture());
        assertEquals("q.demo", captor.getValue().getName());
        assertTrue(captor.getValue().isDurable());
    }

    @Test
    void eliminarColaInexistenteDevuelveFalse() {
        when(rabbitAdmin.getQueueProperties("q.nada")).thenReturn(null);

        assertFalse(service.eliminarCola("q.nada"));
        verify(rabbitAdmin, never()).deleteQueue("q.nada");
    }

    @Test
    void eliminarColaExistenteLaBorra() {
        when(rabbitAdmin.getQueueProperties("q.demo")).thenReturn(new Properties());
        when(rabbitAdmin.deleteQueue("q.demo")).thenReturn(true);

        assertTrue(service.eliminarCola("q.demo"));
    }

    @Test
    void crearExchangeDirectDeclaraDirectExchange() {
        service.crearExchange("ex.demo", "direct", true);

        ArgumentCaptor<Exchange> captor = ArgumentCaptor.forClass(Exchange.class);
        verify(rabbitAdmin).declareExchange(captor.capture());
        assertInstanceOf(DirectExchange.class, captor.getValue());
    }

    @Test
    void crearExchangeTopicDeclaraTopicExchange() {
        service.crearExchange("ex.demo", "topic", true);

        ArgumentCaptor<Exchange> captor = ArgumentCaptor.forClass(Exchange.class);
        verify(rabbitAdmin).declareExchange(captor.capture());
        assertInstanceOf(TopicExchange.class, captor.getValue());
    }
}
