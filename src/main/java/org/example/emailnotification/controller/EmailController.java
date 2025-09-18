package org.example.emailnotification.controller;

import org.example.emailnotification.dto.request.EmailRequestDto;
import org.example.emailnotification.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("send-simple")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody EmailRequestDto request) {
        emailService.sendSimpleEmail(request);
        return ResponseEntity.ok("Email sent successfully.");
    }

}
