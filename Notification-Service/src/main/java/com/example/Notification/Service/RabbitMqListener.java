package com.example.Notification.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

    @RabbitListener(queues = "user.created.notification.queue")
    public void handleUserCreate(UserRequest userRequest) {
        System.out.println("Hoş geldin bildirimi hazırlanıyor: " + userRequest.getEmail());

    }

    @RabbitListener(queues = "${rabbitmq.queue.transfer.name}")
    public void handleTransfer(TransferRequest transferRequest) {
        System.out.println(" Transfer bildirimi hazırlanıyor:");
        System.out.println("→ Gönderen ID: " + transferRequest.getSenderId());
        System.out.println("→ Alıcı ID: " + transferRequest.getReceiverId());
        System.out.println("→ Tutar: " + transferRequest.getAmount());

        simulateCpuLoad("Transfer bildirimi");
        simulateDelay("Transfer bildirimi");
    }

    private void simulateCpuLoad(String taskName) {
        System.out.println(" [CPU] Simülasyon başlıyor: " + taskName);
        long start = System.currentTimeMillis();
        double dummy = 0;
        for (int i = 0; i < 200_000_000; i++) {
            dummy += Math.sqrt(i) * Math.tan(i % 360);
        }
        long end = System.currentTimeMillis();
        System.out.println("✅ [CPU] Simülasyon bitti: " + taskName + " → Süre: " + (end - start) + " ms");
    }

    private void simulateDelay(String taskName) {
        System.out.println(" [DELAY] " + taskName + " için bekleniyor...");
        try {
            Thread.sleep(3000);
            System.out.println(" [DELAY] Bekleme bitti: " + taskName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(" Delay kesildi: " + e.getMessage());
        }
    }
}

