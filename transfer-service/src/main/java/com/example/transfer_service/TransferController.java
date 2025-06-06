package com.example.transfer_service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.IntStream;

import java.util.Random;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    @Value("${rabbitmq.routing.key}")
    private String ROUTING_NAME;



    private final AmqpTemplate amqpTemplate;

    private final Random random = new Random();


    public TransferController(final AmqpTemplate amqpTemplate) {this.amqpTemplate = amqpTemplate;}

    @PostMapping
    public ResponseEntity<String> sendTransfer(@RequestBody TransferRequest request) {
        if (request.getSenderId().equals(request.getReceiverId())) {
            return ResponseEntity.badRequest().body("Sender and receiver cannot be the same.");
        }

        amqpTemplate.convertAndSend(
                EXCHANGE_NAME,ROUTING_NAME, request
        );
        return ResponseEntity.ok("Transfer message sent to RabbitMQ.");

    }


    @GetMapping("/simulate")
    public ResponseEntity<String> simulateMassTransfers() {
        IntStream.range(0, 100).forEach(i -> {
            Long senderId = (long) (random.nextInt(100) + 1);
            Long receiverId;
            do {
                receiverId = (long) (random.nextInt(100) + 1);
            } while (receiverId.equals(senderId));

            int amount = random.nextInt(1000) + 1;

            TransferRequest transferRequest = new TransferRequest();
            transferRequest.setSenderId(senderId);
            transferRequest.setReceiverId(receiverId);
            transferRequest.setAmount(amount);

            amqpTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_NAME, transferRequest);
            System.out.println("🟢 Transfer sent: " + senderId + " → " + receiverId + " | ₺" + amount);
        });

        return ResponseEntity.ok("✅ 100 random transfer messages sent to RabbitMQ.");
    }

    }
