package com.example.Transaction.Processor.Service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.name}")
    private String QUEUE_NAME;

    @Bean
    public Queue queue(){return new Queue(QUEUE_NAME);}

    @Bean
    public MessageConverter messageConverter(){return new Jackson2JsonMessageConverter();}



}
