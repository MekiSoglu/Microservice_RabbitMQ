package com.example.Transaction.Processor.Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransferRequestListener {

    private final TransactionRepository transactionLogRepository;

    public TransferRequestListener(TransactionRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void handleTransfer(TransferRequest transferRequest) {
        System.out.println(" Yeni transfer isteği alındı:");
        System.out.println("→ Gönderen ID: " + transferRequest.getSenderId());
        System.out.println("→ Alıcı ID: " + transferRequest.getReceiverId());
        System.out.println("→ Tutar: " + transferRequest.getAmount());

        simulateHeavyComputation("Transfer loglama öncesi hazırlık");

        TransactionLog log = new TransactionLog();
        log.setSenderId(transferRequest.getSenderId());
        log.setReceiverId(transferRequest.getReceiverId());
        log.setAmount(transferRequest.getAmount());

        transactionLogRepository.save(log);

        simulateDelay("Veritabanı sonrası raporlama");

        System.out.println(" Transfer işlemi loglandı.");
    }

    private void simulateHeavyComputation(String label) {
        System.out.println("🔄 [CPU] Başlatılıyor: " + label);
        long start = System.currentTimeMillis();
        double dummy = 0;
        for (int i = 0; i < 150_000_000; i++) {
            dummy += Math.sin(i) * Math.sqrt(i % 100);
        }
        long end = System.currentTimeMillis();
        System.out.println(" [CPU] Tamamlandı (" + label + ") → Süre: " + (end - start) + " ms");
    }

    private void simulateDelay(String label) {
        System.out.println(" [DELAY] Başlatılıyor: " + label);
        try {
            Thread.sleep(2000); // 2 saniye beklet
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(" Delay kesildi: " + e.getMessage());
        }
        System.out.println(" [DELAY] Tamamlandı: " + label);
    }
}


