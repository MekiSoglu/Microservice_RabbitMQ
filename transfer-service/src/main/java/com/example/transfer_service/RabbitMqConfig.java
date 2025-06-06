package com.example.transfer_service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;



@Configuration
public class RabbitMqConfig {


    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.routing.key}")
    private String ROUTING_NAME;

    @Value("${rabbitmq.notification.queue.name}")
    private String NOTIFICATION_QUEUE_NAME;

    @Value("${rabbitmq.account.queue.name}")
    private String ACCOUNT_QUEUE_NAME;

    @Value("${rabbitmq.transaction.queue.name}")
    private String TRANSACTION_QUEUE_NAME;

    @Bean
    public TopicExchange exchange(){return new TopicExchange(EXCHANGE_NAME);}

    @Bean
    public Queue notificationQueue(){return new Queue(NOTIFICATION_QUEUE_NAME);}

    @Bean
    public Queue accountQueue(){return new Queue(ACCOUNT_QUEUE_NAME);}

    @Bean
    public Queue transactionQueue(){return new Queue(TRANSACTION_QUEUE_NAME);}

    @Bean
    public Binding bindingNotification(@Qualifier("notificationQueue") Queue notificationQueue , TopicExchange topicExchange){
        return BindingBuilder
                .bind(notificationQueue)
                .to(topicExchange)
                .with(ROUTING_NAME);
    }

    @Bean
    public Binding bindingAccount(@Qualifier("accountQueue") Queue accountQueue , TopicExchange topicExchange){
        return BindingBuilder
                .bind(accountQueue)
                .to(topicExchange)
                .with(ROUTING_NAME);
    }

    @Bean
    public Binding transactionAccount(@Qualifier("transactionQueue") Queue transactionQueue , TopicExchange topicExchange){
        return BindingBuilder
                .bind(transactionQueue)
                .to(topicExchange)
                .with(ROUTING_NAME);
    }

    @Bean
    public MessageConverter messageConverter() {return new Jackson2JsonMessageConverter();}

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;

    }

}
