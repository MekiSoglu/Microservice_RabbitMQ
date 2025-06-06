package com.example.Account_Service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random = new Random();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RabbitListener(queues = "${rabbitmq.queue.user.name}")
    public void handleUserCreate(UserRequest userRequest) {
        System.out.println(" Yeni kullanıcı geldi: " + userRequest.getName());

        int initialBalance = random.nextInt(9000) + 1000;

        Account account = new Account();
        account.setUserId(userRequest.getId());
        account.setName(userRequest.getName());
        account.setEmail(userRequest.getEmail());
        account.setBalance(initialBalance);

        accountRepository.save(account);

        System.out.println(" Hesap oluşturuldu: " + userRequest.getName() + ", bakiye: " + initialBalance);
    }

    // Transfer işlemi
    @RabbitListener(queues = "${rabbitmq.queue.transfer.name}")
    public void handleTransfer(TransferRequest transferRequest) {
        System.out.println(" Transfer isteği alındı");

        Account sender = accountRepository.findById(transferRequest.getSenderId())
                                          .orElseThrow(() -> new RuntimeException("Gönderici bulunamadı"));

        Account receiver = accountRepository.findById(transferRequest.getReceiverId())
                                            .orElseThrow(() -> new RuntimeException("Alıcı bulunamadı"));

        if (sender.getBalance() < transferRequest.getAmount()) {
            System.out.println(" Yetersiz bakiye! Transfer reddedildi.");
            return;
        }

        int amount = transferRequest.getAmount();

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        System.out.println(" Transfer tamamlandı: " + amount + "₺");
        System.out.println(" Gönderici: " + sender.getName() + " → Yeni bakiye: " + sender.getBalance() + "₺");
        System.out.println(" Alıcı: " + receiver.getName() + " → Yeni bakiye: " + receiver.getBalance() + "₺");
    }

}

