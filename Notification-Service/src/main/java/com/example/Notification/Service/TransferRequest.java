package com.example.Notification.Service;


public class TransferRequest {
    private Long senderId;
    private Long receiverId;
    private int amount;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(final Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(final Long receiverId) {
        this.receiverId = receiverId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }
}
