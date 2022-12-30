package com.healthcareapp.healthcareappbackend.service;

import com.healthcareapp.healthcareappbackend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
}
