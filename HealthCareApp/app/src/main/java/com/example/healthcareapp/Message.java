package com.example.healthcareapp;

public class Message {
    private String sender, receiver, message;
    private int pfp;

    public Message(String sender, String receiver, String message, int pfp) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.pfp = pfp;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public int getPfp() {
        return pfp;
    }
}
