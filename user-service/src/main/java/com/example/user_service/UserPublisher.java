package com.example.user_service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserPublisher {
    private final RabbitTemplate rabbitTemplate;

    public UserPublisher(final RabbitTemplate rabbitTemplate) {this.rabbitTemplate = rabbitTemplate;}



    public void publishUserCreate(User user){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                user
        );
    }
}
