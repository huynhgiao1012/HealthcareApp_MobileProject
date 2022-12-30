package com.example.healthcareappdoctor;

public class Message {
    private String receiver, sender, message;

    public Message(String receiver, String sender, String message) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
