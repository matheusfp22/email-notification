package org.example.emailnotification.controller;

import jakarta.mail.MessagingException;
import org.example.emailnotification.dto.request.EmailMimeRequestDto;
import org.example.emailnotification.dto.request.EmailRequestDto;
import org.example.emailnotification.service.EmailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "send-mime", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendMimeEmail(@ModelAttribute EmailMimeRequestDto request) throws MessagingException {
        emailService.sendMimeEmail(request);
        return ResponseEntity.ok("Email sent successfully.");
    }

}
