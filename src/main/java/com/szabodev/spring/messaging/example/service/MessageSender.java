package com.szabodev.spring.messaging.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szabodev.spring.messaging.example.config.JmsConfig;
import com.szabodev.spring.messaging.example.dto.ExampleMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MessageSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 15000)
    public void sendMessage1() {
        ExampleMessageDTO message = ExampleMessageDTO
                .builder()
                .id(UUID.randomUUID())
                .message("Test message 1")
                .build();
        System.out.println("=========================");
        System.out.println("MessageSender - Sending message 1: " + message);
        jmsTemplate.convertAndSend(JmsConfig.OUTPUT_QUEUE_1, message);
    }

    @Scheduled(fixedRate = 15000, initialDelay = 5000)
    public void sendMessage2() {
        ExampleMessageDTO message = ExampleMessageDTO
                .builder()
                .id(UUID.randomUUID())
                .message("Test message 2")
                .build();
        System.out.println("=========================");
        System.out.println("MessageSender - Sending message 2: " + message);
        jmsTemplate.convertAndSend(JmsConfig.OUTPUT_QUEUE_2, message);
    }
}
