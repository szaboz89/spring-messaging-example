package com.szabodev.spring.messaging.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szabodev.spring.messaging.example.config.JmsConfig;
import com.szabodev.spring.messaging.example.dto.ExampleMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MessageListener {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.OUTPUT_QUEUE_1)
    public void listen(@Payload ExampleMessageDTO exampleMessage,
                       @Headers MessageHeaders headers) {
        System.out.println("MessageListener - Receiving message 1");
        System.out.println("MessageListener - Headers: " + headers);
        System.out.println("MessageListener - ExampleMessage using @Payload: " + exampleMessage);
    }

    @JmsListener(destination = JmsConfig.OUTPUT_QUEUE_2)
    public void listen(Message message) throws JMSException {
        System.out.println("MessageListener - Receiving message 2");
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                ExampleMessageDTO exampleMessageDTO = objectMapper.readValue(textMessage.getText(), ExampleMessageDTO.class);
                System.out.println("MessageListener - ExampleMessage using objectMapper: " + exampleMessageDTO);
            } catch (JsonProcessingException e) {
                System.out.println("MessageListener - Cannot convert to object: " + textMessage.getText());
            }
        }
    }

    @JmsListener(destination = JmsConfig.SEND_RECEIVE_QUEUE)
    public void listenAndReply(@Payload ExampleMessageDTO exampleMessage,
                               Message message) throws JMSException {
        System.out.println("MessageListener - Receiving message 3");
        System.out.println("MessageListener - ExampleMessage using @Payload: " + exampleMessage);
        ExampleMessageDTO replyMessage = ExampleMessageDTO
                .builder()
                .id(UUID.randomUUID())
                .message(exampleMessage.getMessage() + " <- Received")
                .build();
        System.out.println("MessageListener - Sending reply: " + replyMessage);
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), replyMessage);
    }
}
