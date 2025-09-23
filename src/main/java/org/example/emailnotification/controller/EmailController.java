package org.example.emailnotification.controller;

import org.example.emailnotification.dto.request.EmailMimeRequestDto;
import org.example.emailnotification.dto.request.EmailSimpleRequestDto;
import org.example.emailnotification.producer.EmailProducer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("email")
public class EmailController {

    private final EmailProducer emailProducer;

    public EmailController(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    @PostMapping("send-simple")
    public ResponseEntity<String> sendSimpleEmail(@RequestBody EmailSimpleRequestDto request) {
        emailProducer.sendEmail(request);
        return ResponseEntity.ok("Email sent successfully.");
    }

    @PostMapping(value = "send-mime", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> sendMimeEmail(@ModelAttribute EmailMimeRequestDto request) {
        emailProducer.sendEmail(request);
        return ResponseEntity.ok("Email sent successfully.");
    }

}
