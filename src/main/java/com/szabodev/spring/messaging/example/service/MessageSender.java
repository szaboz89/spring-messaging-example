package com.szabodev.spring.messaging.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szabodev.spring.messaging.example.config.JmsConfig;
import com.szabodev.spring.messaging.example.dto.ExampleMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
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

    @Scheduled(fixedRate = 15000, initialDelay = 10000)
    public void sendAndReceiveMessage() throws JMSException {
        ExampleMessageDTO message = ExampleMessageDTO
                .builder()
                .id(UUID.randomUUID())
                .message("Test send and receive")
                .build();
        System.out.println("=========================");
        System.out.println("MessageSender - Sending message 3: " + message);
        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.SEND_RECEIVE_QUEUE, session -> {
            try {
                Message messageToSend = session.createTextMessage(objectMapper.writeValueAsString(message));
                messageToSend.setStringProperty("_type", "com.szabodev.spring.messaging.example.dto.ExampleMessageDTO");
                return messageToSend;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to convert message");
            }
        });

        System.out.println("MessageSender - Reply received");
        if (receivedMessage instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) receivedMessage;
            System.out.println("MessageSender - Reply message: " + textMessage.getText());
        } else {
            System.out.println("MessageSender - Reply message not received or not TextMessage instance");
        }
    }
}
