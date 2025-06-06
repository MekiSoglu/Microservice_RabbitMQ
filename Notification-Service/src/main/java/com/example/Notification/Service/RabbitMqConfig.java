package com.example.Notification.Service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.user.name}")
    private  String USER_QUEUE_NAME;

    @Bean
    public Queue userQueue(){return new Queue(USER_QUEUE_NAME);}

    @Bean
    public MessageConverter converter(){return new Jackson2JsonMessageConverter();}



    @Value("${rabbitmq.queue.transfer.name}")
    private String TRANSFER_QUEUE_NAME;



    @Bean
    public Queue transferQueue(){return new Queue(TRANSFER_QUEUE_NAME);}

}
