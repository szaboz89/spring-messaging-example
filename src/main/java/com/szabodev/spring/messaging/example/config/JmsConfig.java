package com.szabodev.spring.messaging.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String OUTPUT_QUEUE_1 = "example-output-queue-1";
    public static final String OUTPUT_QUEUE_2 = "example-output-queue-2";
    public static final String SEND_RECEIVE_QUEUE = "example-send-receive-output-queue";

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
