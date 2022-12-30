package com.healthcareapp.healthcareappbackend.controller;

import com.healthcareapp.healthcareappbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
}
