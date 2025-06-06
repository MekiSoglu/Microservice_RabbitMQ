package com.example.user_service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;


@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "user.exchange";
    public static final String ROUTING_KEY = "user.created";
    public static final String NOTIFICATION_QUEUE_NAME = "user.created.notification.queue";

    public static final String ACCOUNT_QUEUE_NAME = "user.created.account.queue";



    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queueAccount(){
        return new Queue(ACCOUNT_QUEUE_NAME);
    }

    @Bean
    public Queue queueNotification(){
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }

    @Bean
    public Binding bindingNotification(@Qualifier("queueNotification") Queue notificationQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(topicExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public Binding bindingAccount(@Qualifier("queueAccount") Queue accountQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(accountQueue)
                .to(topicExchange)
                .with(ROUTING_KEY);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(); // JSON mesaj gönderimi
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
